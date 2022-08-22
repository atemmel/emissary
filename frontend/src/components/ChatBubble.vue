<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatMessage";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const isUser = (id: number) => props.currentUserId === id;

const isImage = () => props.message.attachment 
    && props.message.attachment.type.startsWith("image/");

</script>

<template>
  <div class="bubble-row">
    <div 
      v-if="message.attachment == null"
      :class="{'user': isUser(message.author), 'stranger': !isUser(message.author)}" class="bubble">
      {{message.contents}}
    </div>
    <div v-else-if="isImage()">
      <img 
        src="data:{{message.attachment.type}};base64 {{message.attachment.bytes}}" 
        alt="{{message.attachment.name}}"
      />
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
