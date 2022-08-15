<script setup lang="ts">
defineProps<{
  isUser: boolean;
  contents: string;
}>();
</script>

<template>
  <div class="bubble-row">
    <div v-if="isUser" class="bubble user">
      {{contents}}
    </div>
    <div v-else class="bubble stranger">
      {{contents}}
    </div>
  </div>
</template>

<style scoped>

.bubble-row {
  width: 100%;
  display: inline-block;
  padding: 4px;
}

.bubble {
  padding: 8px 12px;
  border-radius: 16px;
  max-width: 60%;
  font-size: 1.1em;
}

.user {
  background: #77D;
  float: right;
}

.stranger {
  background: #555;
  float: left;
}
</style>
