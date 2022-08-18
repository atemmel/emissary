<script setup lang="ts">
import axios from "axios";
import FriendsListItemComponent from "./FriendsListItemComponent.vue";
import type {FriendsListItem} from "./../models/FriendsListItem";
import {ref, onMounted, watch} from "vue";
import {useStore} from "./../store";

const store = useStore();

const props = defineProps<{
  currentUserId: number;
  friendsListChange: boolean;
}>();

const emit = defineEmits(["conversationChange"]);

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
});

const friendsListItems = ref<FriendsListItem[]>([]);
const currentItemId = ref<number|null>(null);

const getAllFriendsListItems = () => {
  const token = store.state.jwtToken;
  instance.get("/friendslistitems/" + props.currentUserId, {
      headers: {"Authorization": `Bearer ${token}`},
    }).then((response: any) => {
    friendsListItems.value = response.data;
    if(friendsListItems.value.length > 0 && currentItemId.value == null) {
      const newId = friendsListItems.value[0].conversationId;
      changeConversation(newId);
    } 
  }).catch((err: string) => {
    console.log(err);
  });
};

onMounted(() => {
  getAllFriendsListItems();
});

const changeConversation = (id: number) => {
  currentItemId.value = id;
  emit("conversationChange", id);
};

watch(() => props.friendsListChange, 
  async() => {
    getAllFriendsListItems();
  }
);
</script>

<template>
  <div id="friends-list">
    <FriendsListItemComponent 
      v-for="(item, idx) in friendsListItems" 
      :key="idx" 
      :item="item"
      :current-item-id="currentItemId"
      @conversation-change="changeConversation"
    />
  </div>
</template>

<style scoped> 
#friends-list {
  width: 40vw;
  height: 90vh;
  border-right: 1px solid #555;
  border-top: 1px solid #555;
  overflow-y: auto;
}

</style> 
