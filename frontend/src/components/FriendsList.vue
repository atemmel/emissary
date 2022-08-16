<script setup lang="ts">
import axios from "axios";
import FriendsListItemComponent from "./FriendsListItemComponent.vue";
import type {FriendsListItem} from "./../models/FriendsListItem";
import {ref, onMounted} from "vue";

const instance = axios.create({
  baseURL: "http://localhost:8080",
});

const currentId = 4;

const friendsListItems = ref<FriendsListItem[]>([]);

const getAllFriendsListItems = () => {
  instance.get("/friendslistitems/" + currentId).then((response: any) => {
    friendsListItems.value = response.data;
  });
};

onMounted(() => {
  getAllFriendsListItems();
});
</script>

<template>
  <div id="friends-list">
    <FriendsListItemComponent 
      v-for="(item, idx) in friendsListItems" 
      :key="idx" 
      :item="item"
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
