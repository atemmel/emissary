<script setup lang="ts">
import PlusIcon from "./PlusIcon.vue";
</script>

<template>
  <div class="upload-button">
    <PlusIcon></PlusIcon>
  </div>
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
</style>
