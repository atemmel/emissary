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

const contents = ref<string>("");

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
        <textarea v-model="contents" placeholder="Write your message here..."></textarea>
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
