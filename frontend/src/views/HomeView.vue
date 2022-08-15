<script setup lang="ts">
import ChatComponent from "./../components/ChatComponent.vue";
import FriendsList from "./../components/FriendsList.vue";

</script>

<template>
  <div id="content">
    <div id="right-col-wrapper">
      <div id="logo">
        <h1>Emissary ✉</h1>
      </div>
      <FriendsList/>
    </div>
    <ChatComponent/>
  </div>
</template>

<style scoped> 

#content {
  overflow-y: none;
}

#right-col-wrapper {
  vertical-align: top;
  display: inline-block;
}

#logo {
  border-right: 1px solid #555;
  padding: 1em 0em;
  height: 10vh;
  text-align: center;
  vertical-align:middle;
  line-height: 5vh;
}

</style>
