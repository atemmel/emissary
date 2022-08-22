<script setup lang="ts">
import axios from "axios";
import OverlayDialog from "./OverlayDialog.vue";
import {useStore} from "./../store";

const store = useStore();

const props = defineProps<{
  visible: boolean;
  currentUserId: number;
  currentConversationId: number|null;
}>();

const emit = defineEmits(["close", "submit"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const sendClose = () => {
  emit("close");
};

const sendSubmit = () => {
  leaveConversation();
};

const leaveConversation = () => {
  if(props.currentConversationId == null) {
    return;
  }
  const token = store.state.jwtToken;
  instance.post("/conversations/removeParticipants", {
    users: [props.currentUserId],
    conversationId: props.currentConversationId,
    }, {headers: {"Authorization": `Bearer ${token}`}},).then(() => {
      emit("submit");
    });
};
</script>

<template>
  <OverlayDialog :visible="visible" @close="sendClose">
    <div class="dialog-title">
      Are you sure you wish to leave?
    </div>
    <div class="submit-button left" @click="sendClose">No</div>
    <div class="submit-button right" @click="sendSubmit">Yes</div>
  </OverlayDialog>
</template>

<style scoped>

.submit-button {
  padding: 0px 8px;
  background-color: #555;
  border-radius: 0px 0px 0px 16px;
  padding: 8px;
  display: inline-block;
  width: 50%;
}

.left {
  border-radius: 0px 0px 0px 16px;
  border-right: 1px solid #777;
}

.right {
  border-radius: 0px 0px 16px 0px;
  border-left: 1px solid #777;
  background-color: #722;
}

.submit-button:hover {
  background-color: #888;
  cursor: pointer;
}

.right:hover {
  background-color: #A22;
}
</style>
