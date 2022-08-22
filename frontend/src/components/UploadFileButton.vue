<script setup lang="ts">
import PlusIcon from "./PlusIcon.vue";
import type {ChatMessageAttachment} from "./../models/ChatMessage";

const emit = defineEmits(["upload"]);

const fileChanged = () => {
  const files = document.getElementById("file-input").files;
  if(files.length != 1) {
    return;
  }
  const file = files[0];
  const mb = 1024 * 1024 * 1024;
  if(file.size > 8 * mb) {
    //TODO: display error regarding file not being able to be shown
    return;
  }
  console.log(file);
  emit("upload", file);
  /*
  const reader = new FileReader();
  reader.onload = () => {
    const base64bytes = btoa(reader.result);
    emit("upload", {
      name: file.name,
      type: file.type,
      bytes: base64bytes,
    } as ChatMessageAttachment);

  };
  reader.readAsDataURL(file);
  */
};

// 32301bytes ~32kb file became 57456 bytes ~56kb
// 57456 / 32301 = ~1.77876 times increase
// in other words, server should only accept files the size of 
// 8 * 1.8 * 1024 * 1024 bytes
</script>

<template>
  <label for="file-input">
  <div class="upload-button">
    <PlusIcon @click="fileChanged"/>
  </div>
  </label>
  <input type="file" id="file-input" @change="fileChanged">
</template>

<style scoped>

.upload-button {
  float: right;
  background: #444;
  padding: 1em;
  border-radius: 16px;
}

.upload-button:hover {
  background: #555;
  cursor: pointer;
}

#file-input {
  /* better for accessibility than outright removing the element */
  position: absolute; left: -99999rem
}
</style>
