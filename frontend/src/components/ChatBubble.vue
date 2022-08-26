<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatModels";
import {ref} from "vue";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const showTime = ref<boolean>(false);

const isUser = (id: number) => props.currentUserId === id;

const isImage = () => props.message.attachment
  && props.message.attachment.bytes
  && props.message.attachment.type.startsWith("image/");

const isVideo = () => props.message.attachment
  && props.message.attachment.bytes
  && props.message.attachment.type.startsWith("video/");

const isPoll = () => props.message.attachment
  && props.message.attachment.poll;

const src = () => props.message.attachment
  ? `data:${props.message.attachment.type};base64,${props.message.attachment.bytes}`
  : "";

const transformClass = {
  'user': isUser(props.message.author),
  'stranger': !isUser(props.message.author),
}

const bgClass = {
  'user-bg': isUser(props.message.author),
  'stranger-bg': !isUser(props.message.author),
};

const fileSize = () => props.message.attachment
  ? props.message.attachment.bytes.length * (3.0/4.0)
  : 0;

const fileStr = () => {
  const kb = 1024;
  const mb = kb * kb;
  const size = fileSize();
  if(size < kb) {
    return size + " bytes";
  } else if(size < mb) {
    return Math.round(size / kb) + " kb";
  }
  return Math.round(size / mb) + " mb";
};

const download = () => {
  if(props.message.attachment== null) {
    return;
  }
  const a = document.createElement("a"); //Create <a>
  a.href = src();
  a.download = props.message.attachment.name; //File name Here
  a.click(); //Downloaded file
};

const timeStr = new Date(props.message.timestamp)
  .toLocaleTimeString([], { hour: "2-digit", minute: "2-digit"});

const timeTransform = {
  'right': isUser(props.message.author),
};

const toggleTime = () => showTime.value = !showTime.value;
</script>

<template>
  <div class="bubble-row" @click="toggleTime">
    <div class="bubble-wrapper" :class="transformClass">
      <!-- Regular message -->
      <div v-if="message.attachment == null" 
        :class="bgClass"
        class="bubble text">
        {{message.contents}}
      </div>
      <!-- Image -->
      <img v-else-if="isImage()"
        @click="toggleTime"
        class="img bubble"
        :src="src()" 
        :alt="message.attachment ? message.attachment.name : 'Unnamed image'"
      />
      <!-- Video -->
      <div v-else-if="isVideo()" 
        @click="toggleTime"
        class="bubble">
        <video class="video" controls>
          <source :src="src()"/>
        </video>
      </div>
      <!-- Other attachment -->
      <div v-else :class="bgClass" class="bubble text">
        <div class="file" @click="download">
          <div>
            {{message.attachment.name}}
          </div>
          <div>
            📄 {{fileStr()}}
          </div>
        </div>
      </div>
      <div v-show="showTime" :class="timeTransform" class="time">
        {{timeStr}}
      </div>
    </div>
  </div>
</template>

<style scoped>

.bubble-row {
  width: 100%;
  display: inline-block;
  padding: 4px;
}

.bubble-wrapper {
  max-width: 60%;
}

.right {
  text-align: right;
}

.time {
  position: relative;
  padding: 0px 1em;
  font-size: 0.9em;
}

.bubble {
  margin: 0px 12px;
  border-radius: 16px;
  font-size: 1.1em;
}

.user {
  float: right;
}

.user-bg {
  background: #77D;
}

.user-bg:hover {
  background: #88E;
  cursor: pointer;
}

.stranger {
  float: left;
}

.stranger-bg {
  background: #555;
}

.stranger-bg:hover {
  background: #666;
  cursor: pointer;
}

.img {
  max-width: 100%;
  max-height: 200px;
}

.video {
  border-radius: 16px;
  max-width: 100%;
  max-height: 200px;
}

.text {
  padding: 8px 16px;
}

.file:hover {
  cursor: pointer;
}
</style>
