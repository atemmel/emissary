<script setup lang="ts">

defineProps<{
  visible: boolean;
}>();

const emit = defineEmits(["close"]);

const sendEmit = () => {
  emit("close");
};

</script>

<template>
  <div v-show="visible" class="overlay-dialog-wrapper">
    <div class="overlay" @click="sendEmit"></div>
    <div class="new-conversation-dialog">
      <slot />
    </div>
  </div>
</template>

<style scoped>

.overlay-dialog-wrapper {
  position: fixed;
  z-index: 10;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  text-align: center;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.new-conversation-dialog {
  z-index: 100;
  vertical-align: middle;
  display: inline-block;
  border: 1px solid #555;
  border-radius: 16px;
  background-color: var(--color-background);
  color: var(--color-text);
  text-align: center;
  width: 400px;
}

</style>
