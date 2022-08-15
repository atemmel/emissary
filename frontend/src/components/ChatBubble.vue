<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatMessage";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const isUser = (id: number) => props.currentUserId === id;

</script>

<template>
  <div class="bubble-row">
    <div v-if="isUser(message.userId)" class="bubble user">
      {{message.contents}}
    </div>
    <div v-else class="bubble stranger">
      {{message.contents}}
    </div>
  </div>
</template>

<style scoped>

.bubble-row {
  width: 100%;
  display: inline-block;
  padding: 4px;
}

.bubble {
  padding: 8px 12px;
  border-radius: 16px;
  max-width: 60%;
  font-size: 1.1em;
}

.user {
  background: #77D;
  float: right;
}

.stranger {
  background: #555;
  float: left;
}
</style>
