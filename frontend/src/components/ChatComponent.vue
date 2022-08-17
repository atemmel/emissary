<script setup lang="ts">
import axios from "axios";
import type {ChatMessage} from "./../models/ChatMessage";
import ChatBubble from "./../components/ChatBubble.vue";
import {ref, onMounted, watch} from "vue";

const props = defineProps<{
  currentConversationId: number | null;
  currentUserId: number;
}>();

const instance = axios.create({
  baseURL: "http://localhost:8080",
});


/*
import { io } from "socket.io-client";
const socket = io("ws://localhost:8080");

socket.on("dummy", (arg: any) => {
  console.log("Got sent:", arg);
});
*/

import {Client} from "@stomp/stompjs";

const client = new Client({
  brokerURL: "ws://localhost:8080/ws/websocket",
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
});

/*
client.debug = (str: string) => {
  console.log(str);
};
*/

const publish = () => {
  const body = JSON.stringify({
    id: null,
    authorId: props.currentUserId,
    contents: message.value,
    conversation: props.currentConversationId,
  }, null, 2);
  console.log("Sending:", body);
  client.publish({
    destination: "/app/dummy",
    body: body,
  });
};

client.onConnect = () => {
  console.log("Client is connected");
  client.subscribe("/topic/dummy", (msg: any) => {
    const recvMsg = JSON.parse(msg.body);
    console.log("Recieved:", recvMsg);
    chatMessages.value.push(recvMsg);
  });
};

client.activate();

const message = ref<string>("");

const chatMessages = ref<ChatMessage[]>([]);

const getAllMessagesInConversation = () => {
  if(props.currentConversationId == null) {
    return;
  }
  instance.get("/conversations/" + props.currentConversationId).then((response: any) => {
    chatMessages.value = response.data.messages;
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

const sendMessage = (e: Event) => {
  e.preventDefault();
  if(!client.active) {
    return;
  }
  publish();
  message.value = "";
};
</script>

<template>
  <div id="chat">
    <div id="chat-bubbles">
      <ChatBubble 
        v-for="(message, idx) in chatMessages" 
        :key="idx" 
        :message="message" 
        :current-user-id="currentUserId" 
      />
    </div>
    <div id="chat-field-wrapper">
      <div id="chat-field">
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
  height: 90vh;
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
  margin: 0;
  width: 34.5vw;
  overflow: auto;
  background : #555;
  border-radius: 16px;
  color: var(--color-text);
  display: block;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu,
    Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  font-size: 1.1em;
}
</style>
