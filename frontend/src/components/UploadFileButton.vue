<script setup lang="ts">
import PlusIcon from "./PlusIcon.vue";

const fileChanged = () => {
  const files = document.getElementById("file-input").files;
  if(files.length != 1) {
    return;
  }
  console.log(files);
};
</script>

<template>
  <label for="file-input">
  <div class="upload-button">
    <PlusIcon @click="selectFile"/>
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
