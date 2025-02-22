<template>
    <Menubar :model="items">
        <template #item="{ item, props }">
            <router-link v-if="item.route" v-slot="{ href, navigate, isActive }" :to="item.route" custom>
                <a :href="href" v-bind="props.action" @click="navigate">
                    <span :class="item.icon" />
                    <span :class="{ 'text-bold': isActive }">{{ item.label }}</span>
                </a>
            </router-link>
            <Button v-else type="button" :icon="item.icon" :label="item.label" @click="signOut"></Button>
        </template>
    </Menubar>
</template>

<script setup>
    import { Menubar, Button } from 'primevue';
    import { ref } from 'vue'
    import { useAuthStore } from '@/stores/authStore.js'
    import router from '@/router/index.js'

    const authStore = useAuthStore();

    const items = ref([
        {
            label: 'Home',
            icon: 'pi pi-home',
            route: '/home'
        },
        {
            label: 'Publish',
            icon: 'pi pi-plus-circle',
            route: '/publish'
        },
        {
            label: 'My favorites',
            icon: 'pi pi-heart',
            route: '/favorites'
        },
        {
            label: 'My profile',
            icon: 'pi pi-user',
            route: '/my-profile'
        },
        {
            label: 'Sign out',
            icon: 'pi pi-sign-out'
        }
    ]);

    async function signOut() {
        try {
            await authStore.logout();
            await router.push('/');
        } catch (error) {
            console.log(error);
        }
    }
</script>

<style scoped>

</style>
