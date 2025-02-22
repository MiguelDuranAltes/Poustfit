import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura'

import App from './App.vue'
import router from './router'
import { useAuthStore } from '@/stores/authStore.js'

const pinia = createPinia()
const app = createApp(App)

app.use(pinia)

const authStore = useAuthStore();

async function initializeApp() {
    try {
        // Vemos si está autenticado
        await authStore.isAuthenticationChecked();
    } catch (error) {
        console.error('Error during app initialization:', error);
    }
}

initializeApp().then(
    () => {
        app.use(router);
        app.use(PrimeVue, {
            theme: {
                preset: Aura
            }
        })
        app.mount('#app');
    }
);
