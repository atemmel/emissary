<script setup lang="ts">
import type {FriendsListItem} from "./../models/FriendsListItem";

defineProps<{
  item: FriendsListItem;
}>();

</script>
<template>
  <div class="friend-item">
    <div class="friend-title">
      {{item.name}}
    </div>
    <div class="friend-prev-message">
      {{item.prevMessage}}
    </div>
  </div>
</template>
<style scoped>
.friend-item {
  display: block;
  width: 100%;
  border-bottom: 1px solid #555;
}

.friend-title {
  font-weight: bold;
  padding: 1em 2em;
}

.friend-prev-message {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  padding: 1em 2em;
}

.friend-item:hover {
  background-color: #555;
  cursor: pointer;
}
</style>
