<script setup lang="ts">
import type {EmissaryUser} from "./../models/EmissaryUser";
import {useApi} from "./../api";
import {useStore} from "./../store";
import SelectUsersDialog from "./SelectUsersDialog.vue";

defineProps<{
  visible: boolean;
}>();

const emit = defineEmits(["submit"]);

const api = useApi();
const store = useStore();

const onSubmit = (users: EmissaryUser[]) => {
  const doEmit = () => emit("submit");
  const idList = users.map((usr) => usr.id);
  if(idList.length <= 0) {
    return;
  }
  idList.push(store.state.userId);
  api.post("/conversations/create", {
      participants: idList,
      messages: [],
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
