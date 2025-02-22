import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'
import WelcomeView from '@/views/WelcomeView.vue'
import LoginView from '@/features/login/views/LoginView.vue'
import HomeView from '@/features/home/views/HomeView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/welcome',
        },
        {
            path: '/welcome',
            name: 'welcomePage',
            component: WelcomeView,
            meta: { public: true }
        },
        {
            path: '/register',
            name: 'register',
            component: WelcomeView,
            meta: { public: true }
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
            meta: { public: true }
        },
        {
            path: '/home',
            name: 'homePage',
            component: HomeView,
            meta: { public: false }
        },
    ],
})

router.beforeEach((to, from, next) => {

    const authStore = useAuthStore();

    if (to.meta.public) {
        if (authStore.isAuthenticated){
            next("/home");
        } else{
            next();
        }
    } else {
        if (authStore.isAuthenticated) {
            next();
        } else {
            next('/');
        }
    }
})

export default router
