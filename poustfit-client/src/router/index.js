import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'
import WelcomeView from '@/views/WelcomeView.vue'
import LoginView from '@/features/login/views/LoginView.vue'
import HomeView from '@/features/home/views/HomeView.vue'
import RegisterView from '@/features/register/views/RegisterView.vue'
import MyProfileView from '@/features/my-profile/views/MyProfileView.vue'
import PublishView from '@/features/publish/views/PublishView.vue'
import FavoritesView from '@/features/favorites/views/FavoritesView.vue'
import DetailsView from '@/features/details/view/DetailsView.vue'

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
            component: RegisterView,
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
        {
            path: '/home/:postId',
            name: 'homeDetailsPage',
            component: DetailsView,
            meta: { public: false }
        },
        {
            path: '/my-profile',
            name: 'profilePage',
            component: MyProfileView,
            meta: { public: false }
        },
        {
            path: '/publish',
            name: 'publishPage',
            component: PublishView,
            meta: { public: false }
        },
        {
            path: '/favorites',
            name: 'favoritesPage',
            component: FavoritesView,
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
