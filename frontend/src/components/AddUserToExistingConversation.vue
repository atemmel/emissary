<script setup lang="ts">
import SelectUsersDialog from "./SelectUsersDialog.vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
defineProps<{
  visible: boolean;
}>();

const onSubmit = (users: EmissaryUser[]) => {
  console.log("awoogah awoogah");
};

</script>

<template>
  <SelectUsersDialog 
    :visible="visible"
    title="Select users to invite" 
    submit-title="Invite"
    @submit="onSubmit"
  />
</template>

<style scoped> 
</style>
