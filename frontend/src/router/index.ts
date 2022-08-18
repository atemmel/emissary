import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ChatView from '../views/ChatView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", name: "", component: HomeView },
    { path: "", name: "", component: HomeView },
    { path: "/chat", name: "chat", component: ChatView },
  ]
})

export default router
