<script setup lang="ts">
import type {ChatMessage} from "./../models/ChatModels";
import PollBar from "./PollBar.vue";
import {ref} from "vue";

const props = defineProps<{
  message: ChatMessage;
  currentUserId: number;
}>();

const emit = defineEmits(["vote"]);

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
  'poll': isPoll(),
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

const totalVotes = () => {
  if(!props.message.attachment || !props.message.attachment.poll) {
    return 0;
  }
  const poll = props.message.attachment.poll;
  let sum = 0;
  for(const key in poll ) {
    const value = poll[key];
    sum += value;
  }
  return sum;
};

const calcWidth = (votes: number) => {
  const total = totalVotes();
  if(total == 0) {
    return 0;
  }
  return votes / total * 100.0;
};

const toggleTime = () => showTime.value = !showTime.value;

const emitVote = (what: string) => emit("vote", what, props.message);
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
      <!-- Poll -->
      <div v-else-if="isPoll()"
        @click="toggleTime"
        :class="bgClass"
        class="bubble text">
        <div class="center-text">{{message.attachment.name}}</div>
        <div v-for="(item, idx) in message.attachment.poll" :key="idx">
          <PollBar 
            :name="idx as string"
            :width="calcWidth(item)" 
            :votes="item"
            @vote="emitVote"
          />
        </div>
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

.poll {
  margin: 0 auto;
  float: none;
}

.text {
  padding: 8px 16px;
}

.center-text {
  text-align: center;
}

.file:hover {
  cursor: pointer;
}
</style>
