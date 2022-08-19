<script setup lang="ts">
import axios from "axios";
import AddUserToConversationItem from "./../components/AddUserToConversationItem.vue";
import OverlayDialog from "./../components/OverlayDialog.vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
import {ref, watch, defineEmits} from "vue";
import {useStore} from "./../store";

const props = defineProps<{
  visible: boolean;
}>();

const store = useStore();

const emit = defineEmits(["close", "submit"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const chattableUsers = ref<EmissaryUser[]>([]);

const addedUsers = ref<EmissaryUser[]>([]);

const getListOfChattableUsers = () => {
  const token = store.state.jwtToken;
  instance.get("/users/", {
    headers: {"Authorization": `Bearer ${token}`},
  }).then((response: any) => {
    const users = response.data as EmissaryUser[];
    chattableUsers.value = users.filter((usr) => usr.id != store.state.userId);
  }).catch((err: string) => {
    console.log(err);
  });
};

watch(() => props.visible,
  async () => {
    if(props.visible) {
      addedUsers.value = chattableUsers.value = [];
      getListOfChattableUsers();
    }
  }
);

const sendEmit = () => {
  addedUsers.value = [];
  emit("close");
};

const addUser = (who: EmissaryUser) => {
  addedUsers.value.push(who);
};

const removeUser = (who: EmissaryUser) => {
  const idx = addedUsers.value.indexOf(who);
  if(idx == -1) {
    return;
  }
  addedUsers.value.splice(idx, 1);
};

const submit = () => {
  const idList = addedUsers.value.map((usr) => usr.id);
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
  }).then(() => {
    emit("submit");
    console.log("Good");
  }).catch(() => {
    console.log("Bad");
  });
};

const maySubmit = () => addedUsers.value.length > 0;

</script>

<template>
  <OverlayDialog :visible="visible" @close="sendEmit">
    <div class="new-conversation-dialog-title">
      New conversation
    </div>
    <div class="mini-added-list">
      <div v-for="(user, idx) in addedUsers" :key="idx" class="mini-added-list-item">
        {{user.name}}
      </div>
    </div>
    <div class="conversation-list">
      <AddUserToConversationItem 
        v-for="(user, idx) in chattableUsers" 
        :user="user" 
        :key="idx"
        @added="addUser"
        @removed="removeUser"
      />
    </div>
    <div v-show="maySubmit()" 
      class="submit-conversation-button" 
      @click="submit">
        Start conversation
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
