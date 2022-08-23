<script setup lang="ts">
import {ref} from "vue";
import type {EmissaryUser} from "./../models/EmissaryUser";
const props = defineProps<{
  user: EmissaryUser;
}>();

const emit = defineEmits(["added", "removed"]);

const isAdded = ref<boolean>(false);

const toggle = () => {
  isAdded.value = !isAdded.value;
  if(isAdded.value) {
    emit("added", props.user);
  } else {
    emit("removed", props.user);
  }
}

</script>

<template>
  <div class="name-div" :class="{added: isAdded}" @click="toggle">
    {{user.name}}
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

.added {
  background-color: #888;
}
</style>
