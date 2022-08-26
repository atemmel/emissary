<script setup lang="ts">
import {emojiTable} from "./../emojis";

defineProps<{
  visible: boolean;
}>();

const emit = defineEmits(["pick"]);

const onClick = (what: string) => emit("pick", what);

</script>

<template>
  <div v-show="visible" class="bg">
    <div class="cell" 
      v-for="(_, key) in emojiTable" 
      :key="key"
      @click="onClick(key)">
      {{key}}
    </div>
  </div>
</template>

<style scoped>

.bg {
  position: absolute;
  bottom: 5em;
  background: #444;
  width: 300px;
  left: -200px;
  padding: 4px;
  border-radius: 16px;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr;
  max-height: 400px;
  overflow-y: auto;
}

.cell {
  position: inline-block;
  border-radius: 16px;
  text-align: center;
  padding: 8px;
}

.cell:hover {
  background: #555;
  cursor: pointer;
}

</style>
