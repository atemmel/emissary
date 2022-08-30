<script setup lang="ts">
import OverlayDialog from "./OverlayDialog.vue";
import {useApi} from "./../api";

const props = defineProps<{
  visible: boolean;
  currentUserId: number;
  currentConversationId: number|null;
}>();

const emit = defineEmits(["close", "submit"]);

const api = useApi();

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
  api.post("/conversations/removeParticipants", {
    users: [props.currentUserId],
    conversationId: props.currentConversationId,
    }).then(() => {
      emit("submit");
    });
};
</script>

<template>
  <OverlayDialog :visible="visible" @close="sendClose">
    <div class="dialog-title">
      Are you sure you wish to leave?
    </div>
    <div class="submit-button button-left" @click="sendClose">No</div>
    <div class="submit-button button-right evil" @click="sendSubmit">Yes</div>
  </OverlayDialog>
</template>

<style scoped>

.evil {
  background-color: #722;
}

.evil:hover {
  background-color: #A22;
}
</style>
