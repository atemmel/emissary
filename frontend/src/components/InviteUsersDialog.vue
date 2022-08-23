<script setup lang="ts">
import axios from "axios";
import SelectUsersDialog from "./SelectUsersDialog.vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
import {useStore} from "./../store";
const props = defineProps<{
  visible: boolean;
  chatParticipants: number[];
  currentConversationId: number | null;
}>();

const store = useStore();

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

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
  const token = store.state.jwtToken;
  instance.post("/conversations/addParticipants", {
      users: participants,
      conversationId: props.currentConversationId,
    }, {headers: {"Authorization": `Bearer ${token}`}});
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
