<script setup lang="ts">
import {ref} from "vue";
import ChatBubble from "./../components/ChatBubble.vue";

const contents = ref<string>("");

const lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacinia vulputate ipsum at maximus. Phasellus nibh nunc, condimentum a vestibulum sed, accumsan non sem. Sed.";

interface Message {
  isUser: boolean;
  contents: string,
}

const messages: Message[] = [
  { isUser: true, contents: lorem } as Message,
  { isUser: false, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: false, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
  { isUser: true, contents: lorem } as Message,
];

interface FriendChat {
  name: string;
  prevMessage: string;
}

const friendChats: FriendChat[] = [
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
  { name: "Name", prevMessage: lorem } as FriendChat,
];

</script>

<template>
  <div id="content">
    <div id="right-col-wrapper">
      <div id="logo">
        <h1>Emissary ✉</h1>
      </div>
      <div id="contacts">
        <div v-for="(friendChat, idx) in friendChats" :key="idx" class="contact">
          <div class="friend-title">
            {{friendChat.name}}
          </div>
          <div class="friend-prev-message">
            {{friendChat.prevMessage}}
          </div>
        </div>
      </div>
    </div>
    <div id="chat">
      <div id="chat-bubbles">
        <ChatBubble v-for="(message, idx) in messages" :key="idx" :isUser="message.isUser" :contents="message.contents"/>
      </div>
      <div id="chat-field-wrapper">
        <div id="chat-field">
          <textarea v-model="contents" placeholder="Write your message here..."></textarea>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped> 

#content {
  overflow-y: none;
}

#right-col-wrapper {
  vertical-align: top;
  display: inline-block;
}

#logo {
  border-right: 1px solid #555;
  padding: 1em 0em;
  height: 10vh;
  text-align: center;
  vertical-align:middle;
  line-height: 5vh;
}

#contacts {
  width: 40vw;
  height: 90vh;
  border-right: 1px solid #555;
  border-top: 1px solid #555;
  overflow-y: auto;
}

.contact {
  display: block;
  width: 100%;
  border-bottom: 1px solid #555;
}

.friend-title {
  font-weight: bold;
  padding: 1em 2em;
}

.friend-prev-message {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  padding: 1em 2em;
}

.contact:hover {
  background-color: #555;
  cursor: pointer;
}

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
