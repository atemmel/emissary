<script setup lang="ts">
import ChatComponent from "./../components/ChatComponent.vue";
import FriendsList from "./../components/FriendsList.vue";
import CreateNewConversationDialog from "./../components/CreateNewConversationDialog.vue";
import {ref} from "vue";
import router from "./../router";
import {useStore} from "./../store";
import {tryReadSessionIntoStore} from "./../session";
import {Client} from "@stomp/stompjs";
import InviteUsersDialog from "../components/InviteUsersDialog.vue";
import LeaveConversationDialog from "../components/LeaveConversationDialog.vue";
import CreatePollDialog from "../components/CreatePollDialog.vue";

const store = useStore();

const client = ref<Client>(new Client({
  brokerURL: "ws://localhost:8080/ws/websocket",
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
}));


const currentConversationId = ref<number|null>(null);

const chatParticipants = ref<number[]>([]);

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

const updateFriendsList = () => {
  friendsListChange.value = !friendsListChange.value;
};

const createNewConversationDialogVisible = ref<boolean>(false);

const inviteUserDialogVisible = ref<boolean>(false);

const leaveConversationDialogVisible = ref<boolean>(false);

const createPollDialogVisible = ref<boolean>(false);

const hasConversation = () => currentConversationId.value != null;

const onOpenNewConversationDialog = () => {
  createNewConversationDialogVisible.value = true;
};

const onCloseNewConversationDialog = () => {
  createNewConversationDialogVisible.value = false;
};

const onSubmitNewConversation = () => {
  onCloseNewConversationDialog();
  updateFriendsList();
};

const onOpenInviteUserDialog = (participants: number[]) => {
  inviteUserDialogVisible.value = true;
  chatParticipants.value = participants;
  updateFriendsList();
};

const onCloseInviteUserDialog = () => {
  inviteUserDialogVisible.value = false;
};

const onSubmitInviteUserDialog = () => {
  onCloseInviteUserDialog();
  updateFriendsList();
};

const onOpenLeaveConversationDialog = () => {
  leaveConversationDialogVisible.value = true;
};

const onCloseLeaveConversationDialog = () => {
  leaveConversationDialogVisible.value = false;
};

const onSubmitLeaveConversationDialog = () => {
  onCloseLeaveConversationDialog();
  currentConversationId.value = null;
  updateFriendsList();
};

const onOpenCreatePollDialog = () => {
  createPollDialogVisible.value = true;
}

const onCloseCreatePollDialog = () => {
  createPollDialogVisible.value = false;
}
const onSubmitCreatePollDialog = () => {
  onCloseCreatePollDialog();
}

</script>

<template>
  <div v-if="currentUserId" id="content">
    <CreateNewConversationDialog
      :visible="createNewConversationDialogVisible" 
      @close="onCloseNewConversationDialog" 
      @submit="onSubmitNewConversation"
    />
    <!-- The chatParticipants.value should not work -->
    <InviteUsersDialog
      :visible="inviteUserDialogVisible"
      :chat-participants="chatParticipants.value"
      :current-conversation-id="currentConversationId"
      @close="onCloseInviteUserDialog"
      @submit="onSubmitInviteUserDialog"
    />
    <LeaveConversationDialog
      :visible="leaveConversationDialogVisible"
      :current-user-id="currentUserId"
      :current-conversation-id="currentConversationId"
      @close="onCloseLeaveConversationDialog"
      @submit="onSubmitLeaveConversationDialog"
    />
    <CreatePollDialog
      :client="client"
      :visible="createPollDialogVisible"
      :current-conversation-id="currentConversationId"
      :current-user-id="currentUserId"
      @close="onCloseCreatePollDialog"
      @submit="onSubmitCreatePollDialog"
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
      :client="client"
      :current-conversation-id="currentConversationId" 
      :current-user-id="currentUserId" 
      @newMessage="updateFriendsList"
      @open-invite-user-dialog="onOpenInviteUserDialog"
      @open-leave-dialog="onOpenLeaveConversationDialog"
      @open-create-poll-dialog="onOpenCreatePollDialog"
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
