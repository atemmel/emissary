<script setup lang="ts">
import PlusIcon from "./PlusIcon.vue";

const emit = defineEmits(["upload"]);

const fileChanged = () => {
  const element = document.getElementById("file-input");
  if(element == null || element.value == "") {
    return;
  }
  const files = element.files;
  if(files.length != 1) {
    return;
  }
  const file: File = files[0];
  const mb = 1024 * 1024;
  if(file.size > 8 * mb) {
    //TODO: display error regarding file not being able to be shown
    return;
  }
  emit("upload", file);
  element.value = "";
};

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
