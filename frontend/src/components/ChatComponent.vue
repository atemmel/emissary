<script setup lang="ts">
import axios from "axios";
import type {ChatMessage, ChatHead} from "./../models/ChatModels";
import ChatBubble from "./../components/ChatBubble.vue";
import UploadFileButton from "./../components/UploadFileButton.vue";
import {ref, onMounted, watch, nextTick} from "vue";
import {Client} from "@stomp/stompjs";
import type {Message} from "@stomp/stompjs";
import {useStore} from "./../store";

const store = useStore();

const props = defineProps<{
  currentConversationId: number | null;
  currentUserId: number;
}>();

const emit = defineEmits(["newMessage", "openInviteUserDialog", "openLeaveDialog"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const message = ref<string>("");

const chatMessages = ref<ChatMessage[]>([]);
const chatParticipants = ref<number[]>([]);

const head = ref<ChatHead|null>(null);

const isPageinating = ref<boolean>(true);
const pageinationEnd = ref<boolean>(false);

const client = new Client({
  brokerURL: "ws://localhost:8080/ws/websocket",
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
});

client.onConnect = () => {
  console.log("Client is connected");
  client.subscribe("/chat", (msg: Message) => {
    if(!msg.body) {
      return;
    }
    const recvMsg = JSON.parse(msg.body);
    chatMessages.value.push(recvMsg);
    emit("newMessage");
  });
  client.subscribe("/chat/head", (msg: Message) => {
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
    const delta = newHead.conversationHead - head.value.conversationHead;
    if(delta > 0) {
      const from = head.value.conversationHead;
      catchup(from);
    }

    // handle friendslist
    if(newHead.friendsListHead > head.value.friendsListHead) {
      emit("newMessage");
    }

    // set new head
    head.value = newHead;
  });
  setInterval(headPing, 3000);
};

client.activate();

const initConversation = () => {
  if(props.currentConversationId == null) {
    return;
  }
  isPageinating.value = true;
  pageinationEnd.value = false;
  const firstHead = 20;
  const token = store.state.jwtToken;
  instance.get("/conversations/" 
    + props.currentConversationId 
    + "/init/?count="
    + firstHead,
    {headers: {"Authorization": `Bearer ${token}`},},
  ).then((response: any) => {
    chatMessages.value = response.data.messages;
    chatParticipants.value = response.data.participants;
    head.value = {
      friendsListHead: new Date(),
      conversationHead: response.data.messages.length,
    } as ChatHead;
    isPageinating.value = false;
  });
};

const scrollToBottom = () => {
    const bubbles = document.getElementById("chat-bubbles");
    if(bubbles != null) {
      bubbles.scrollTop = bubbles.scrollHeight;
    }
};

const pageinate = () => {
  if(head.value == null || isPageinating.value || pageinationEnd.value) {
    return;
  }
  isPageinating.value = true;
  const delta = 10;
  const thisPage = head.value.conversationHead;
  const nextPage = thisPage + delta;

  const token = store.state.jwtToken;
  instance.get("/conversations/"
    + props.currentConversationId
    + "/history/?from="
    + thisPage
    + "&to="
    + nextPage,
    {headers: {"Authorization": `Bearer ${token}`}})
  .then((response: any) => {
    if(head.value == null) {
      return;
    }
    const prefix = response.data as ChatMessage[];
    const suffix = chatMessages.value;
    chatMessages.value = prefix.concat(suffix);
    head.value.conversationHead += prefix.length;
    pageinationEnd.value = prefix.length < delta;
    isPageinating.value = false;
  });
};

const catchup = (from: number) => {
  if(props.currentConversationId == null) {
    return;
  }
  const token = store.state.jwtToken;
  const url = "/conversations/" 
    + props.currentConversationId
    + "/catchup?from="
    + from;
  instance.get(url, 
    {headers: {"Authorization": `Bearer ${token}`}},
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
  if(!client.active || !props.currentConversationId) {
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

  client.publish({
    destination: "/chat/send",
    body: JSON.stringify(msg),
  });
  message.value = "";
};

const headPing = () => {
  if(!client.active || !props.currentConversationId) {
    return;
  }
  client.publish({
    destination: "/chat/askhead/" + props.currentConversationId,
    body: JSON.stringify(props.currentConversationId),
  });
};

const onUpload = (file: File) => {
  if(!client.active || !props.currentConversationId) {
    return;
  }

  const formData = new FormData();
  formData.append("userId", props.currentUserId.toString());
  formData.append("conversationId", props.currentConversationId.toString());
  formData.append("file", file);
  const token = store.state.jwtToken;
  instance.post("/attachments/create", formData, {
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "multipart/form-data",
    },
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
    if(bubbles.scrollTop != 0 || !scrollingUp) {
      return;
    }

    pageinate();
};

onMounted(() => {
  initConversation();
});

watch(() => props.currentConversationId,
  async () => {
    initConversation();
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
      />
      <div id="bubbles-anchor"></div>
    </div>
    <div id="chat-field-wrapper">
      <div id="chat-field">
        <UploadFileButton 
          @upload="onUpload"
        />
        <textarea 
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
