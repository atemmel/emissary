<script setup lang="ts">
import SelectUsersDialog from "./SelectUsersDialog.vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
import {useApi} from "./../api";
const props = defineProps<{
  visible: boolean;
  chatParticipants: number[];
  currentConversationId: number | null;
}>();

const api = useApi();

const onSubmit = (users: EmissaryUser[]) => {
  const participants = users.map((user) => user.id);
  postNewParticipants(participants);
};

const noDuplicates = (user: EmissaryUser) => {
  if(props.chatParticipants.indexOf(user.id) == -1) {
    return true;
  }
  return false;
}

const postNewParticipants = (participants: number[]) => {
  if(props.currentConversationId == null) {
    return;
  }
  api.post("/conversations/addParticipants", {
      users: participants,
      conversationId: props.currentConversationId,
    });
};

</script>

<template>
  <SelectUsersDialog 
    :visible="visible"
    title="Select users to invite" 
    submit-title="Invite"
    :filter="noDuplicates"
    @submit="onSubmit"
  />
</template>

<style scoped> 
</style>
