<script setup lang="ts">
import type {ChatMessage, ChatHead, ChatCaches, ChatCache, PollVote, Poll, ChatMessageAttachment} from "./../models/ChatModels";
import ChatBubble from "./../components/ChatBubble.vue";
import EmojiButton from "./../components/EmojiButton.vue";
import CreatePollButton from "./../components/CreatePollButton.vue";
import UploadFileButton from "./../components/UploadFileButton.vue";
import {ref, onMounted, watch, nextTick} from "vue";
import type {Client, Message} from "@stomp/stompjs";
import {useApi} from "../api";

const props = defineProps<{
  client: Client;
  currentConversationId: number | null;
  currentUserId: number;
}>();

const client = ref<Client>(props.client);

const emit = defineEmits(["newMessage", "openInviteUserDialog", "openLeaveDialog", "openCreatePollDialog"]);

const api = useApi();

const message = ref<string>("");

const chatCaches = ref<ChatCaches>(new Map([]));
const chatMessages = ref<ChatMessage[]>([]);
const chatParticipants = ref<number[]>([]);

const head = ref<ChatHead|null>(null);

const isPageinating = ref<boolean>(true);
const pageinationEnd = ref<boolean>(false);

client.value.onConnect = () => {
  console.log("Client is connected");
  client.value.subscribe("/chat", (msg: Message) => {
    if(!msg.body) {
      return;
    }
    const obj = JSON.parse(msg.body);
    if(obj.message) {
      const recvMsg = obj.message as ChatMessage;
      if(recvMsg.conversation == props.currentConversationId) {
        chatMessages.value.push(recvMsg);
      }
      emit("newMessage");

    } else if(obj.vote) {
      const vote = obj.vote as PollVote;
      handleVote(vote);
    } else {
      console.log("Did not handle message");
    }
  });
  client.value.subscribe("/chat/head", (msg: Message) => {
    if(props.currentConversationId == null) {
      return;
    }
    if(msg.body == null) {
      console.log("Head ping failed on arrival");
      return;
    }

    if(head.value == null) {
      console.log("Ignoring head ping");
      return;
    }

    const newHead = JSON.parse(msg.body) as ChatHead;

    // handle conversation
    const newConversationHead = newHead.conversationHeads[props.currentConversationId];
    const oldConversationHead = head.value.conversationHeads[props.currentConversationId];
    const delta = newConversationHead - oldConversationHead;
    if(delta > 0) {
      const from = oldConversationHead;
      catchup(from);
    }

    if(newHead.friendsListHead > head.value.friendsListHead) {
      emit("newMessage");
    }

    head.value.conversationHeads[props.currentConversationId] = newConversationHead;
  });
  setInterval(headPing, 3000);
  setInterval(pruneStaleCaches, 5000);
};

client.value.activate();

const initConversation = () => {
  if(props.currentConversationId == null) {
    return;
  }

  isPageinating.value = true;
  pageinationEnd.value = false;
  const firstHead = 20;
  api.get("/conversations/" 
    + props.currentConversationId 
    + "/init/?count="
    + firstHead,
  ).then((response: any) => {
    head.value = {
      friendsListHead: new Date(),
      conversationHeads: {}
    } as ChatHead;
    if(props.currentConversationId == null) {
      return;
    }
    chatMessages.value = response.data.messages;
    chatParticipants.value = response.data.participants;
    saveCache(props.currentConversationId);
    head.value.conversationHeads[props.currentConversationId] = response.data.messages.length;
    isPageinating.value = false;
  });
};

const scrollToBottom = () => {
  const bubbles = document.getElementById("chat-bubbles");
  if(bubbles != null) {
    bubbles.scrollTop = bubbles.scrollHeight;
  }
};

const handleVote = (vote: PollVote) => {
  api.get("/messages/" + vote.poll).then(
  (response: any) => {
    const id = response.data.id;
    const idx = chatMessages.value.findIndex((msg: ChatMessage) => id == msg.id);
    if(idx == null) {
      return;
    }
    chatMessages.value[idx] = response.data;
  });
}

const pageinate = () => {
  if(head.value == null 
      || isPageinating.value 
      || pageinationEnd.value 
      ||props.currentConversationId == null) {
    return;
  }
  isPageinating.value = true;
  const delta = 10;
  const thisPage = head.value.conversationHeads[props.currentConversationId];
  const nextPage = thisPage + delta;

  console.log("fetching from", thisPage, "to", nextPage);
  api.get("/conversations/"
    + props.currentConversationId
    + "/history/?from="
    + thisPage
    + "&to="
    + nextPage)
  .then((response: any) => {
    if(head.value == null || props.currentConversationId == null) {
      return;
    }
    const prefix = response.data as ChatMessage[];
    const suffix = chatMessages.value;
    chatMessages.value = prefix.concat(suffix);
    head.value.conversationHeads[props.currentConversationId] += prefix.length;
    pageinationEnd.value = prefix.length < delta;
    isPageinating.value = false;
  });
};

const catchup = (from: number) => {
  if(props.currentConversationId == null) {
    return;
  }
  const url = "/conversations/" 
    + props.currentConversationId
    + "/catchup?from="
    + from;
  api.get(url, 
  ).then((response: any) => {
    if(!response || !response.data) {
      return;
    }
    const newMessages = response.data as ChatMessage[];
    for(const msg of newMessages) {
      chatMessages.value.push(msg);
    }
  });
};

const sendMessage = (e: Event) => {
  e.preventDefault();
  if(!client.value.active || !props.currentConversationId) {
    return;
  }

  const msg: ChatMessage = {
    id: null,
    author: props.currentUserId,
    contents: message.value,
    conversation: props.currentConversationId,
    timestamp: new Date(),
    attachment: null,
  };

  client.value.publish({
    destination: "/chat/send",
    body: JSON.stringify(msg),
  });
  message.value = "";
};

const headPing = () => {
  if(!client.value.active || !props.currentConversationId) {
    return;
  }
  client.value.publish({
    destination: "/chat/askhead/" + props.currentConversationId,
    body: JSON.stringify(props.currentConversationId),
  });
};

const onUpload = (file: File) => {
  if(!client.value.active || !props.currentConversationId) {
    return;
  }

  const formData = new FormData();
  formData.append("userId", props.currentUserId.toString());
  formData.append("conversationId", props.currentConversationId.toString());
  formData.append("file", file);
  api.postHeaders("/attachments/create", formData, {
      "Content-Type": "multipart/form-data",
  });
  headPing();
};

const prevScrollY = ref<number>(1);

const onScroll = () => {
    const bubbles = document.getElementById("chat-bubbles");
    if(bubbles == null) {
      return;
    }

    const scrollingUp = bubbles.scrollTop - prevScrollY.value <= 0;
    prevScrollY.value = bubbles.scrollTop;
    if(bubbles.scrollTop > 100 || !scrollingUp) {
      return;
    }

    pageinate();
};

const onEmojiPicked = (what: string) => {
  message.value += what;
};

const onVote = (what: string, where: number) => {
  if(props.currentConversationId == null) {
    return;
  }
  const vote: PollVote = {
    id: null,
    author: props.currentUserId,
    choice: what,
    poll: where,
  };

  client.value.publish({
    destination: "/chat/vote",
    body: JSON.stringify(vote),
  });
};

onMounted(() => {
  initConversation();
});

const saveCache = (id: number) => {
  console.log("init", id);
  chatCaches.value.set(id, {
    messages: chatMessages.value,
    timestamp: new Date(),
  } as ChatCache);
};

const loadCache = (id: number) => {
  const cache = chatCaches.value.get(id);
  if(cache == null) {
    console.log("Loading nonexistent cache:", id);
    return;
  }
  chatMessages.value = cache.messages;
  cache.timestamp = new Date();
  chatCaches.value.set(id, cache);
};

const pruneStaleCaches = () => {
  const threshold = 1000 * 60 * 3;
  const now = Date.now();
  const currentConversation = props.currentConversationId;
  for(const [key, value] of chatCaches.value) {
    if(key == currentConversation) {
      value.timestamp = new Date(now);
      chatCaches.value.set(key, value);
      continue;
    }
    const then = value.timestamp.getTime();
    const delta = now - then;
    if(delta > threshold) {
      chatCaches.value.delete(key);
    }
  }
};

watch(() => props.currentConversationId,
  async () => {
    if(props.currentConversationId == null || head.value == null) {
      return;
    }
    // cache check
    const cached = chatCaches.value.get(props.currentConversationId) != undefined;
    if(cached) {
      // load cache instead
      loadCache(props.currentConversationId);
      headPing();
    } else {
      initConversation();
    }
  }
);

const getRecentDate = () => chatMessages.value.length == 0
  ? new Date(0)
  : chatMessages.value[chatMessages.value.length - 1].timestamp;

watch(getRecentDate,
  async () => nextTick(scrollToBottom),
);

const emitInviteUser = () => {
  emit("openInviteUserDialog", chatParticipants);
};

const emitLeave = () => {
  emit("openLeaveDialog");
};

const emitCreatePoll = () => {
  emit("openCreatePollDialog");
};

</script>

<template>
  <div id="chat">
    <div id="chat-options" v-show="currentConversationId != null">
      <div class="chat-option-wrapper" @click="emitInviteUser">
        Invite to chat
      </div>
      <div class="chat-option-wrapper" @click="emitLeave">
        <span class="evil">Leave Chat</span>
      </div>
    </div>
    <div id="chat-bubbles" @wheel="onScroll">
      <ChatBubble 
        v-for="(message, idx) in chatMessages" 
        :key="idx" 
        :message="message" 
        :current-user-id="currentUserId" 
        @vote="onVote"
      />
      <div id="bubbles-anchor"></div>
    </div>
    <div id="chat-field-wrapper">
      <div id="chat-field">
        <UploadFileButton @upload="onUpload"/>
        <CreatePollButton @poll-create="emitCreatePoll"/>
        <EmojiButton @pick="onEmojiPicked"/>
        <textarea 
          id="chat-textarea"
          v-model="message" 
          placeholder="Write your message here..." 
          v-on:keypress.enter.exact="sendMessage">
        </textarea>
      </div>
    </div>
  </div>
</template>

<style scoped> 

#chat {
  display: inline-block;
  vertical-align: top;
  width: 60vw;
  min-height: 100vh;
}

#chat-bubbles {
  padding: 10px;
  overflow-y: auto;
  overflow-x: none;
  height: 85vh;
}

#chat-field-wrapper {
  padding: 1em 0em;
  width: 100%;
}

#chat-field {
  padding: 0em 32px 0px 0px;
  width: 100%;
}

#chat-field>textarea {
  float: right;
  resize: none;
  border: none;
  padding: 8px 0px 0px 12px;
  margin-right: 8px;
  width: 34.5vw;
  overflow: auto;
  background : #444;
  border-radius: 16px;
  color: var(--color-text);
  display: block;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu,
    Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  font-size: 1.1em;
}

#chat-options {
  width: 100%;
  border-bottom: 1px solid #555;
  text-align: right;
}

.chat-option-wrapper {
  display: inline-block;
  border-left: 1px solid #555;
  padding: 14px 20px;
}

.chat-option-wrapper:hover {
  background : #555;
  cursor: pointer;
}

.evil {
  color: #F22;
}
</style>
