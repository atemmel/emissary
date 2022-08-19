<script setup lang="ts">
import ChatComponent from "./../components/ChatComponent.vue";
import FriendsList from "./../components/FriendsList.vue";
import CreateNewConversation from "./../components/CreateNewConversation.vue";
import {ref} from "vue";
import router from "./../router";
import {useStore} from "./../store";
import {tryReadSessionIntoStore} from "./../session";

const store = useStore();

const currentConversationId = ref<number|null>(null);

if(!store.state.userId) {
  tryReadSessionIntoStore();
}

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

const visibleDialog = ref<boolean>(false);

const hasConversation = () => currentConversationId.value != null;

const onOpenNewConversationDialog = () => {
  visibleDialog.value = true;
};

const onCloseNewConversationDialog = () => {
  visibleDialog.value = false;
};

const onSubmitNewConversation = () => {
  onCloseNewConversationDialog();
  onFriendsListChange();
};

</script>

<template>
  <div v-if="currentUserId" id="content">
    <CreateNewConversation 
      :visible="visibleDialog" 
      @close="onCloseNewConversationDialog" 
      @submit="onSubmitNewConversation"
    />
    <div id="right-col-wrapper">
      <div id="logo">
        <h1>Emissary ✉</h1>
      </div>
      <div id="new-conversation-button" @click="onOpenNewConversationDialog">
        New conversation
      </div>
      <FriendsList 
        @conversation-change="onConverationChange" 
        :current-user-id="currentUserId" 
        :friends-list-change="friendsListChange"
      />
    </div>
    <ChatComponent 
      v-if="hasConversation()" 
      :current-conversation-id="currentConversationId" 
      :current-user-id="currentUserId" 
      @friends-list-change="onFriendsListChange"
    />
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
</style>
