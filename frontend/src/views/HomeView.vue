<script setup lang="ts">
import {ref} from "vue";
import {useStore} from "./../store";
import {useAuthApi} from "./../api";
import {useRouter} from "vue-router";
import { tryReadSessionIntoStore } from "@/session";

const api = useAuthApi();
const store = useStore();
const router = useRouter();

tryReadSessionIntoStore();
if(store.state.userId) {
  router.push("/chat");
}

const username = ref<string>("");
const password = ref<string>("");

const isLogin = ref<boolean>(true);

const formError = ref<string>("");

const isAuthorizing = ref<boolean>(false);

const toggleForm = () => {
  isLogin.value = !isLogin.value;
};

const login = () => {
  auth("/login", {
    username: username.value,
    password: password.value,
  });
};

const register = () => {
  auth("/register", {
    name: username.value,
    password: password.value,
  });
};

const auth = (url: string, data: any) => {
  isAuthorizing.value = true;
  api.post(url, data).then((response: any) => {
    if(response.data.error){
      formError.value = response.data.error;
      isAuthorizing.value = false;
      return;
    }
    handleRepsonse(response.data);
  }).catch((error: string) => {
      formError.value = error;
      isAuthorizing.value = false;
  });
};

const handleRepsonse = (data: any) => {
  store.commit("setToken", data["jwt-token"]);
  store.commit("setId", data.userId);
  router.push("/chat");
  isAuthorizing.value = false;
};

</script>
<template>
  <header>
    <h1>Emissary ✉</h1>
  </header>
  <div v-if="!isAuthorizing" id="login-wrapper">
    <div v-if="isLogin" class="form-box">
      <div class="title">
        <h1>Login</h1>
      </div>
      <div class="form-element">
        <div class="form-label">
          <label for="username">Username</label>
        </div>
        <input v-model="username" id="username">
      </div>
      <div class="form-element">
        <div class="form-label">
          <label for="password">Password</label>
        </div>
        <input v-model="password" id="password" type="password">
      </div>
      <div class="vpad">
        <div class="form-element" @click="login">
          <div class="button">Login</div>
        </div>
      </div>
      <div>
        {{formError}}
      </div>
      <div id="form-switch">
        Don't have an account? <span @click="toggleForm" class="link">Register here.</span>
      </div>
    </div>
    <div v-else class="form-box">
      <div class="title">
        <h1>Register</h1>
      </div>
      <div class="form-element">
        <div class="form-label">
          <label for="username">Username</label>
        </div>
        <input v-model="username" id="username">
      </div>
      <div class="form-element">
        <div class="form-label">
          <label for="password">Password</label>
        </div>
        <input v-model="password" id="password" type="password">
      </div>
      <div class="vpad">
        <div class="form-element" @click="register">
          <div class="button">Register</div>
        </div>
      </div>
      <div>
        {{formError}}
      </div>
      <div id="form-switch">
        Already have an account? <span @click="toggleForm" class="link">Login here.</span>
      </div>
    </div>
  </div>
</template>
<style scoped>

#login-wrapper {
  width: 1024px;
  margin: 0 auto;
  margin-top: 40px;
}

.form-box {
  border: 1px solid #555;
  padding: 8px 12px;
  border-radius: 16px;
  width: 400px;
  float: right;
}

.form-element {
  padding: 8px;
  width: 100%;
}

.form-label {
  padding: 4px 12px;
}

input {
  width: 100%;
  border: 1px solid #555;
  padding: 8px 12px;
  border-radius: 16px;
  background-color: var(--color-background);
  color: var(--color-text);
  font-size: 1em;
}

input:focus {
  background-color: #555;
  outline: none;
}

.title {
  text-align: center;
}

.button {
  width: 100%;
  font-size: 1.2em;
  border: 1px solid #555;
  padding: 6px 18px;
  border-radius: 16px;
  background-color: var(--color-background);
  color: var(--color-text);
  text-align: center;
  font-weight: bold;
}

.button:hover {
  background-color: #555;
  cursor: pointer;
}

.vpad {
  padding: 22px 0 0 0;
}

.link {
  font-weight: bold;
}

.link:hover {
  color: white;
  text-decoration: underline;
  cursor: pointer;
}

header {
  border-bottom: 1px solid #555;
  padding: 1em;
}

</style>
