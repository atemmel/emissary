<script setup lang="ts">
import {defineProps} from "vue";

defineProps<{
  username: string;
}>();
</script>

<template>
  <div class="name-div">
    {{username}}
  </div>
</template>

<style scoped>
.name-div {
  padding: 1em 0em;
  border-top: 1px solid #555;
}

.name-div:hover {
  background-color: #555;
  cursor: pointer;
}
</style>
