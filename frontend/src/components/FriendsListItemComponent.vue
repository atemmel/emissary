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

const hasAuthor = () => props.item.prevAuthor.length > 0;

</script>
<template>
  <div class="friend-item" @click="select" :class="{'friend-item-selected': selected()}">
    <div class="friend-title">
      {{item.friendName}}
    </div>
    <div class="friend-prev">
      <span class="prev-author" v-show="hasAuthor()">
        {{item.prevAuthor}}: 
      </span>
      <span :class="{'prev-message': !hasAuthor()}">
        {{item.prevMessage}}
      </span>
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

.friend-prev {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  padding: 1em 2em;
}

.prev-message {
  font-style: italic;
}

.friend-item:hover {
  background-color: #555;
  cursor: pointer;
}

.friend-item-selected {
  background-color: #444;
}

.prev-author {
  color: #EEE;
}
</style>
