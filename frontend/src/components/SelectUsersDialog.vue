<script setup lang="ts">
import axios from "axios";
import AddUserToConversationItem from "./AddUserToConversationItem.vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
import OverlayDialog from "./OverlayDialog.vue";
import {ref, watch, defineEmits} from "vue";
import {useStore} from "./../store";

const props = defineProps<{
  visible: boolean;
  title: string;
  submitTitle: string;
}>();

const store = useStore();

const emit = defineEmits(["close", "submit"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const allUsers = ref<EmissaryUser[]>([]);

const selectedUsers = ref<EmissaryUser[]>([]);

const getAllUsersNotSelf = () => {
  const token = store.state.jwtToken;
  instance.get("/users/", {
    headers: {"Authorization": `Bearer ${token}`},
  }).then((response: any) => {
    const users = response.data as EmissaryUser[];
    allUsers.value = users.filter((usr) => usr.id != store.state.userId);
  }).catch((err: string) => {
    console.log(err);
  });
};

watch(() => props.visible,
  async () => {
    if(props.visible) {
      selectedUsers.value = allUsers.value = [];
      getAllUsersNotSelf();
    }
  }
);

const sendClose = () => {
  selectedUsers.value = [];
  emit("close");
};

const addUser = (who: EmissaryUser) => {
  selectedUsers.value.push(who);
};

const removeUser = (who: EmissaryUser) => {
  const idx = selectedUsers.value.indexOf(who);
  if(idx == -1) {
    return;
  }
  selectedUsers.value.splice(idx, 1);
};

const maySubmit = () => selectedUsers.value.length > 0;

const sendSubmit = () => {
  emit("submit", selectedUsers.value);
};

</script>

<template>
  <OverlayDialog :visible="visible" @close="sendClose">
    <div class="new-conversation-dialog-title">
      {{title}}
    </div>
    <div class="mini-added-list">
      <div v-for="(user, idx) in selectedUsers" :key="idx" class="mini-added-list-item">
        {{user.name}}
      </div>
    </div>
    <div class="conversation-list">
      <AddUserToConversationItem 
        v-for="(user, idx) in allUsers" 
        :user="user" 
        :key="idx"
        @added="addUser"
        @removed="removeUser"
      />
    </div>
    <div v-show="maySubmit()" 
      class="submit-conversation-button" 
      @click="sendSubmit">
        {{submitTitle}}
    </div>
  </OverlayDialog>
</template>

<style scoped>
.new-conversation-dialog-title {
  padding: 26px 18px;
  font-weight: bold;
}

.conversation-list {
  overflow-y: auto;
  max-height: 80vh;
}

.mini-added-list {
  max-height: 10vh;
  overflow-y: auto;
  text-align: left;
  padding-left: 20px;
}

.mini-added-list-item {
  display: inline-block;
  padding: 0px 8px;
  margin: 2px;
  background-color: #888;
  border-radius: 16px;
}

.submit-conversation-button {
  padding: 0px 8px;
  background-color: #555;
  border-radius: 0px 0px 16px 16px;
  padding: 8px;
}

.submit-conversation-button:hover {
  background-color: #888;
  cursor: pointer;
}
</style>
