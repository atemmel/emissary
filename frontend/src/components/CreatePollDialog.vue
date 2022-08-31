<script setup lang="ts">
import OverlayDialog from "./OverlayDialog.vue";
import {ref} from "vue";
import type {Client} from "@stomp/stompjs";
import type { ChatMessage, ChatMessageAttachment, Poll } from "../models/ChatModels";

const props = defineProps<{
  currentConversationId: number | null;
  currentUserId: number;
  visible: boolean;
  client: Client;
}>();

const emit = defineEmits(["close", "submit"]);

const choices = ref<string[]>([]);

const choice = ref<string>("");
const title = ref<string>("");

const addChoice = () => {
  const str = choice.value.trim();
  if(str.length == 0) {
    return;
  }
  if(choices.value.indexOf(str) != -1) {
    return;
  }
  choices.value.push(str);
  choice.value = "";
}

const sendClose = () => {
  choices.value = [];
  emit("close");
};

const sendSubmit = () => {
  if(choices.value.length >= 2) {
    post();
    emit("submit");
  }
};

const post = () => {
  const name = title.value.trim();
  if(name.length < 1) {
    return;
  }
  const poll = choices.value.reduce((a, v) => ({ ...a, [v]: 0}), {}) as Poll;

  title.value = "";
  choices.value = [];

  const msg = {
    id: 0,
    contents: "",
    author: props.currentUserId,
    conversation: props.currentConversationId,
    timestamp: new Date(),
    attachment: {
      name: name,
      type: "",
      bytes: "",
      poll: poll,
    } as ChatMessageAttachment,
  } as ChatMessage;

  props.client.publish({
    destination: "/chat/send",
    body: JSON.stringify(msg),
  });
};

const maySubmit = () => choices.value.length >= 2 
    && title.value.trim().length >= 1;
</script>

<template>
  <OverlayDialog :visible="visible" @close="sendClose">
    <div class="dialog-title">
      Create new poll
    </div>
    <input class="input title-input" v-model="title" placeholder="Title">
    <div v-for="(choice, idx) in choices" :key="idx" class="mini-added-list-item" >
      {{choice}}
    </div>
    <div class="choice-input-wrapper">
      <input class="input choice-input" 
        v-model="choice" 
        @keypress.enter.exact="addChoice" 
        placeholder="Choice">
      <div class="choice-submit" @click="addChoice">
        <div class="plus-vertical"></div>
        <div class="plus-horizontal"></div>
      </div>
    </div>
    <div v-show="maySubmit()">
      <div class="submit-button button-left" @click="sendClose">Cancel</div>
      <div class="submit-button button-right" @click="sendSubmit">Create Poll</div>
    </div>
  </OverlayDialog>
</template>

<style scoped>

.choice-input-wrapper {
  width: 300px;
  margin: 1em auto;
}

.input {
  display: inline-block;
  background-color: #444;
  color: var(--color-text);
  border: none;
  font-size: 1em;
  border-radius: 16px;
}

.title-input {
  padding: 8px 25px 8px 25px;
}

.choice-input {
  padding: 8px 34px 8px 16px;
  margin-right: -32px;
}

.choice-submit {
  display: inline-block;
  background-color: #444;
  color: var(--color-text);
  padding: 8px 16px;
  width: 32px;
  height: 32px;
  border-radius: 16px;
  top: 10px;
}

.choice-submit:hover {
  background-color: #555;
  cursor: pointer;
}

.plus-vertical {
  position: absolute;
  background-color: var(--color-text);
  height: 4px;
  width: 16px;
  top: 14px;
  right: 8px;
}

.plus-horizontal {
  position: absolute;
  background-color: var(--color-text);
  width: 4px;
  height: 16px;
  top: 8px;
  right: 14px;
}
</style>
