<script setup lang="ts">
import EmojiPicker from "./EmojiPicker.vue";
import {ref}from "vue";

const pickerVisible = ref<boolean>(false);

const emit = defineEmits(["pick"]);

const onClick = () => pickerVisible.value = !pickerVisible.value;

const onPick = (what: string) => emit("pick", what);

</script>

<template>
<div class="wrapper">
  <EmojiPicker 
    :visible="pickerVisible"
    @pick="onPick"
  />
  <div class="button" @click="onClick">
    <div class="button-wrapper">
      <div class="left eye"></div>
      <div class="right eye"></div>
      <div class="mouth"></div>
    </div>
  </div>
</div>
</template>

<style scoped>

.wrapper {
  float: right;
  margin-right: 8px;
}

.button {
  background: #444;
  padding: 1em;
  border-radius: 16px;
}

.button:hover {
  background: #555;
  cursor: pointer;
}

.button-wrapper {
  position: relative;
  display: inline-block;
  width: 32px;
  height: 32px;
}

.eye {
  background-color: var(--color-text);
  border-radius: 16px;
  position: absolute;
  top: 4px;
  height: 8px;
  width: 8px;
}

.left {
  left: 4px;
}

.right {
  right: 4px;
}

.mouth {
  background-color: var(--color-text);
  border-bottom-left-radius: 100% 200%;
  border-bottom-right-radius: 100% 200%;
  position: absolute;
  top: 22px;
  left: 4px;
  height: 10px;
  width: 24px;
}

.mouth:hover {
  border-bottom-right-radius: 100% 300%;
}

</style>
