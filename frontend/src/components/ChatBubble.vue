<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatModels";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const isUser = (id: number) => props.currentUserId === id;

const isImage = () => props.message.attachment
    && props.message.attachment.bytes
    && props.message.attachment.type.startsWith("image/");

const isVideo = () => props.message.attachment
    && props.message.attachment.bytes
    && props.message.attachment.type.startsWith("video/");

const src = () => props.message.attachment
    ? `data:${props.message.attachment.type};base64,${props.message.attachment.bytes}`
    : "";

const transformClass = {
    'user': isUser(props.message.author),
    'stranger': !isUser(props.message.author),
}

const transformBgClass = {
    'user user-bg': isUser(props.message.author),
    'stranger stranger-bg': !isUser(props.message.author),
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
</script>

<template>
  <div class="bubble-row">
    <div>
      <!-- Regular message -->
      <div v-if="message.attachment == null" 
        :class="transformBgClass"
        class="bubble">
        {{message.contents}}
      </div>
      <!-- Image -->
      <img v-else-if="isImage()"
        class="img"
        :class="transformClass"
        :src="src()" 
        :alt="message.attachment ? message.attachment.name : 'null'"
        />
      <!-- Video -->
      <video v-else-if="isVideo()" :class="transformClass" class="video bubble" controls>
        <source :src="src()"/>
      </video>
      <!-- Other attachment -->
      <div v-else :class="transformBgClass" class="bubble">
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
  transition: 1.2s;
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
  max-height: 200px;
}

.video {
  max-width: 100%;
  max-height: 200px;
}

.file:hover {
  cursor: pointer;
}
</style>
