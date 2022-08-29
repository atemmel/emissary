<script setup lang="ts">

const emit = defineEmits(["pollCreate"]);

const onClick = () => emit("pollCreate");

</script>

<template>
  <div class="wrapper">
    <div class="button" @click="onClick">
      <div class="button-wrapper">
        <div class="bar bar-1"></div>
        <div class="bar bar-2"></div>
        <div class="bar bar-3"></div>
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

.bar {
  background-color: var(--color-text);
  position: absolute;
  height: 8px;
  width: 8px;
}

.bar-1 {
  width: 32px;
}

.bar-2 {
  width: 8px;
  top: 14px;
}

.bar-3 {
  width: 16px;
  top: 28px;
}

</style>
