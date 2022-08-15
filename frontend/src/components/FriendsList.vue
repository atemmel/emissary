<script setup lang="ts">

import FriendsListItemComponent from "./FriendsListItemComponent.vue";
import type {FriendsListItem} from "./../models/FriendsListItem";

const lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacinia vulputate ipsum at maximus. Phasellus nibh nunc, condimentum a vestibulum sed, accumsan non sem. Sed.";

const friendsList: FriendsListItem[] = [
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
  { name: "Name", prevMessage: lorem } as FriendsListItem,
];

</script>

<template>
  <div id="friends-list">
    <FriendsListItemComponent 
      v-for="(item, idx) in friendsList" 
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
