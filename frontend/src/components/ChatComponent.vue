<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatMessage";
import ChatBubble from "./../components/ChatBubble.vue";
import {ref} from "vue";

const contents = ref<string>("");

const lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacinia vulputate ipsum at maximus. Phasellus nibh nunc, condimentum a vestibulum sed, accumsan non sem. Sed.";

const currentUserId = ref<number>(0);

const messages: ChatMessage[] = [
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 1} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 1} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 1} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
  { contents: lorem, userId: 1} as ChatMessage,
  { contents: lorem, userId: 0} as ChatMessage,
];

</script>

<template>
  <div id="chat">
    <div id="chat-bubbles">
      <ChatBubble 
        v-for="(message, idx) in messages" 
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
  max-height: 90vh;
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
