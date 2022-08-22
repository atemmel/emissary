<script setup lang="ts">
import axios from "axios";
import type {ChatMessage} from "./../models/ChatMessage";
import ChatBubble from "./../components/ChatBubble.vue";
import PlusIcon from "./../components/PlusIcon.vue";
import {ref, onMounted, watch, nextTick} from "vue";
import {Client} from "@stomp/stompjs";
import {useStore} from "./../store";

const store = useStore();

const props = defineProps<{
  currentConversationId: number | null;
  currentUserId: number;
}>();

const emit = defineEmits(["newMessage", "openAddUserDialog"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const client = new Client({
  brokerURL: "ws://localhost:8080/ws/websocket",
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
});

client.onConnect = () => {
  console.log("Client is connected");
  client.subscribe("/chat", (msg: any) => {
    const recvMsg = JSON.parse(msg.body);
    chatMessages.value.push(recvMsg);

    emit("newMessage");
  });
};

client.activate();

const message = ref<string>("");

const chatMessages = ref<ChatMessage[]>([]);
const chatParticipants = ref<number[]>([]);

const getAllMessagesInConversation = () => {
  if(props.currentConversationId == null) {
    return;
  }
  const token = store.state.jwtToken;
  instance.get("/conversations/" + props.currentConversationId,
    {headers: {"Authorization": `Bearer ${token}`},},
  ).then((response: any) => {
    console.log(response.data);
    chatMessages.value = response.data.messages;
    chatParticipants.value = response.data.participants;
  });
};

onMounted(() => {
  getAllMessagesInConversation();
});

watch(() => props.currentConversationId,
  async () => {
    getAllMessagesInConversation();
  }
);

watch(() => chatMessages.value.length,
  async () => {
    nextTick(() => {
      const bubbles = document.getElementById("chat-bubbles");
      if(bubbles != null) {
        bubbles.scrollTop = bubbles.scrollHeight;
      }
    });
  }
);

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
  };

  client.publish({
    destination: "/chat/send",
    body: JSON.stringify(msg),
  });
  message.value = "";
};

const emitAddUser = () => {
  emit("openAddUserDialog", chatParticipants);
};
</script>

<template>
  <div id="chat">
    <div id="chat-options" v-show="currentConversationId != null">
      <div class="chat-option-wrapper" @click="emitAddUser">
          Invite to chat
      </div>
      <div class="chat-option-wrapper">
          <span class="evil">Leave Chat</span>
      </div>
    </div>
    <div id="chat-bubbles">
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
        <div id="upload-button">
          <PlusIcon></PlusIcon>
        </div>
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

#upload-button {
  float: right;
  background : #444;
  padding: 1em;
  border-radius: 16px;
}

#upload-button:hover {
  background : #555;
  cursor: pointer;
}

.evil {
  color: #F22;
}
</style>
