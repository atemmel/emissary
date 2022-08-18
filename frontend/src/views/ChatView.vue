<script setup lang="ts">
import axios from "axios";
import ChatComponent from "./../components/ChatComponent.vue";
import FriendsList from "./../components/FriendsList.vue";
import AddUserToConversationItem from "./../components/AddUserToConversationItem.vue";
import {ref} from "vue";
import {useStore} from "./../store";
import type {EmissaryUser} from "./../models/EmissaryUser";
import router from "./../router";

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const store = useStore();

const currentConversationId = ref<number|null>(null);
const currentUserId = store.state.userId;
if(!currentUserId) {
  router.push("/");
}

const onConverationChange = (newId: number) => {
  currentConversationId.value = newId;
};

const friendsListChange = ref<boolean>(false);

const onFriendsListChange = () => {
  friendsListChange.value = !friendsListChange.value;
};

const hasConversation = () => currentUserId != null;

const openNewConversationDialog = () => {
  const overlay = document.getElementById("overlay");
  overlay.style.display = "block";
  getListOfChattableUsers();
};

const closeNewConversationDialog = () => {
  const overlay = document.getElementById("overlay");
  overlay.style.display = "none";
};

const chattableUsers = ref<EmissaryUser[]>([]);

const getListOfChattableUsers = () => {
  const token = store.state.jwtToken;
  instance.get("/users/", {
    headers: {"Authorization": `Bearer ${token}`},
  }).then((response: any) => {
    const users = response.data as EmissaryUser[];
    chattableUsers.value = users;
  }).catch((err: string) => {
    console.log(err);
  });
};

</script>

<template>
  <div v-if="currentUserId" id="content">
    <div id="overlay" @click="closeNewConversationDialog">
      <div id="new-conversation-dialog">
        <div class="new-conversation-dialog-title">
          New conversation
        </div>
        <AddUserToConversationItem 
          v-for="(user, idx) in chattableUsers" 
          :username="user.name" 
          :key="idx"
        />
      </div>
    </div>
    <div id="right-col-wrapper">
      <div id="logo">
        <h1>Emissary ✉</h1>
      </div>
      <div id="new-conversation-button" @click="openNewConversationDialog">
        New conversation
      </div>
      <FriendsList @conversation-change="onConverationChange" :current-user-id="currentUserId" :friends-list-change="friendsListChange"/>
    </div>
    <ChatComponent v-if="hasConversation" :current-conversation-id="currentConversationId" :current-user-id="currentUserId" @friends-list-change="onFriendsListChange"/>
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

#new-conversation-button {
  border-top: 1px solid #555;
  border-right: 1px solid #555;
  padding: 1em 0em;
  text-align: center;
  font-weight: bold;
}

#new-conversation-button:hover {
  background-color: #555;
  cursor: pointer;
}

#overlay {
  position: fixed;
  z-index: 999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: none;
  text-align: center;
}

#new-conversation-dialog {
  vertical-align: middle;
  display: inline-block;
  border: 1px solid #555;
  border-radius: 16px;
  background-color: var(--color-background);
  color: var(--color-text);
  text-align: center;
  width: 400px;
  overflow-y: auto;
  max-height: 80vh;
}

.new-conversation-dialog-title {
  padding: 26px 18px;
  font-weight: bold;
}
</style>
