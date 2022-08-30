<script setup lang="ts">
const props = defineProps<{
  name: string;
  width: number;
  votes: number;
}>();

const emit = defineEmits(["vote"]);

const votesStr = () => props.votes == 1 ? "vote" : "votes";

const emitVote = () => emit("vote", props.name);

</script>

<template>
    <div class="bar-title" @click="emitVote">
      {{name}}
    </div>
  <div class="outer">
    <div class="bar-wrapper">
      <div class="bar" :style="{'width': width + '%'}" @click="emitVote"></div>
    </div>
    <div class="bar-votes">
      {{width}}% <br>({{votes}} {{votesStr()}})
    </div>
  </div>
</template>

<style scoped>
.bar {
  height: 2em;
  width: 0%;
  background-color: var(--color-text);
  border-radius: 16px;
  transition: width 0.25s;
}

.bar:hover {
  background-color: #EEE;
}

.bar-wrapper {
  width: 91%;
  display: inline-block;
  vertical-align: middle;
}

.bar-title {
  display: inline-block;
}

.bar-votes {
  display: inline-block;
  width: 9%;
  vertical-align: middle;
}

.outer {
  padding: 4px;
}
</style>
