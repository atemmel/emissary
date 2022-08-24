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

const isPlaintext = () => attachment
    && attachment.bytes;

const isVideo = () => attachment
    && attachment.bytes
    && attachment.type.startsWith("video/");

const src: string = attachment
    ? `data:${attachment.type};base64,${attachment.bytes}`
    : "";

const transformClass = () => {
  return {
    'user': isUser(props.message.author),
    'stranger': !isUser(props.message.author),
  };
}

const transformBgClass = () => {
  return {
    'user user-bg': isUser(props.message.author),
    'stranger stranger-bg': !isUser(props.message.author),
  };
};

const fileSize = () => attachment
    ? attachment.bytes.length * (3.0/4.0)
    : 0;

const fileStr = () => {
    const size = fileSize();
    const kb = 1024;
    const mb = kb * kb;
    if(size < kb) {
      return size + " bytes";
    } else if(size < mb) {
      return Math.round(size / kb) + " kb";
    }
    return Math.round(size / mb) + " mb";
};

const download = () => {
  if(attachment == null) {
    return;
  }
  const a = document.createElement("a"); //Create <a>
  a.href = `data:${attachment.type};base64,${attachment.bytes}`; //Image Base64 Goes here
  a.download = attachment.name; //File name Here
  a.click(); //Downloaded file
};

</script>

<template>
  <div class="bubble-row">
    <div>
      <!-- Regular message -->
      <div v-if="message.attachment == null" 
        :class="transformBgClass()"
        class="bubble">
        {{message.contents}}
      </div>
      <!-- Image -->
      <img v-else-if="isImage()"
        class="img"
        :class="transformClass()"
        :src="src" 
        :alt="message.attachment ? message.attachment.name : 'null'"
        />
      <!-- Video -->
      <video v-else-if="isVideo()" :class="transformClass()" class="bubble" controls>
        <source :src="src"/>
      </video>
      <!-- Other attachment -->
      <div v-else :class="transformBgClass()" class="bubble">
        <div class="file" @click="download">
          <div>
            {{message.attachment.name}}
          </div>
          <div>
            📄 {{fileStr()}}
          </div>
        </div>
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

.file:hover {
  cursor: pointer;
}
</style>
