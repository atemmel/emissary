<script setup lang="ts">
import type {FriendsListItem} from "./../models/FriendsListItem";

const props = defineProps<{
  item: FriendsListItem;
  currentItemId: number|null;
}>();

const emit = defineEmits(["conversationChange"]);

const select = () => {
    emit("conversationChange", props.item.conversationId);
};

const selected = () => props.currentItemId 
  && props.item.conversationId == props.currentItemId;

</script>
<template>
  <div v-if="selected()" class="friend-item friend-item-selected" @click="select">
    <div class="friend-title">
      {{item.friendName}}
    </div>
    <div class="friend-prev-message">
      {{item.prevMessage}}
    </div>
  </div>
  <div v-else class="friend-item" @click="select">
    <div class="friend-title">
      {{item.friendName}}
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

.friend-item-selected {
  background-color: #444;
}
</style>
