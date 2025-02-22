<template>
    <div class="flex flex-col h-screen p-5 gap-5">
        <NavBar v-if="authStore.isAuthenticated"></NavBar>
        <div class="flex-1 w-full">
            <RouterView :key="$route.fullPath"/>
        </div>
    </div>
</template>

<script setup>
    import { RouterView } from 'vue-router'
    import NavBar from '@/features/navigation/components/NavBar.vue'
    import { useAuthStore } from '@/stores/authStore.js'

    const authStore = useAuthStore();
</script>

<style scoped>
</style>
