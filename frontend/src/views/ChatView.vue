<script setup lang="ts">
import ChatComponent from "./../components/ChatComponent.vue";
import FriendsList from "./../components/FriendsList.vue";
import {ref} from "vue";
import {useStore} from "./../store";
import router from "./../router";

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

</script>

<template>
  <div v-if="currentUserId" id="content">
    <div id="right-col-wrapper">
      <div id="logo">
        <h1>Emissary ✉</h1>
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

</style>
