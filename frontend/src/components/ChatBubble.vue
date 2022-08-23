<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatMessage";
import {unref} from "vue";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const attachment = unref(props.message.attachment);

const isUser = (id: number) => props.currentUserId === id;

const isImage = () => attachment 
    && attachment.bytes
    && attachment.type.startsWith("image/");

const src: string = attachment
    ? `data:${attachment?.type};base64,${attachment?.bytes}`
    : "";

</script>

<template>
  <div class="bubble-row">
    <div>
      <div v-if="message.attachment == null" 
        :class="{'user user-bg': isUser(message.author), 'stranger stranger-bg': !isUser(message.author)}"
        class="bubble">
        {{message.contents}}
      </div>
      <img v-else-if="isImage()"
        class="img"
        :class="{'user': isUser(message.author), 'stranger': !isUser(message.author)}"
        :src="src" 
        :alt="message.attachment ? message.attachment.name : 'null'"
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
  float: right;
}

.user-bg {
  background: #77D;
}

.stranger {
  float: left;
}

.stranger-bg {
  background: #555;
}

.img {
  border-radius: 16px;
  max-width: 100%;
}
</style>
