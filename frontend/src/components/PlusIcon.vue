<script setup lang="ts">
</script>

<template>
  <div class="plus-wrapper">
    <div class="plus-vertical"></div>
    <div class="plus-horizontal"></div>
  </div>
</template>

<style scoped>
.plus-wrapper {
  position: relative;
  display: inline-block;
  width: 32px;
  height: 32px;
}

.plus-vertical {
  position: absolute;
  top: 14px;
  height: 4px;
  width: 32px;
  background-color: var(--color-text);
  border-radius: 3px;
}

.plus-horizontal {
  position: absolute;
  left: 14px;
  height: 32px;
  width: 4px;
  border-radius: 3px;
  background-color: var(--color-text);
}
</style>
