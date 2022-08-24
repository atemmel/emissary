<script setup lang="ts">
import axios from "axios";
import type {EmissaryUser} from "./../models/EmissaryUser";
import {useStore} from "./../store";
import SelectUsersDialog from "./SelectUsersDialog.vue";

defineProps<{
  visible: boolean;
}>();

const emit = defineEmits(["submit"]);

const store = useStore();

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const onSubmit = (users: EmissaryUser[]) => {
  const doEmit = () => emit("submit");
  const idList = users.map((usr) => usr.id);
  if(idList.length <= 0) {
    return;
  }
  idList.push(store.state.userId);
  const token = store.state.jwtToken;
  instance.post("/conversations/create", {
      participants: idList,
      messages: [],
    }, {
    headers: {"Authorization": `Bearer ${token}`},
  }).then(() => doEmit())
  .catch(() => doEmit());
};

</script>

<template>
  <SelectUsersDialog 
    :visible="visible"
    title="New conversation" 
    submit-title="Start conversation"
    :filter="null"
    @submit="onSubmit"
  />
</template>

<style scoped> 
</style>
