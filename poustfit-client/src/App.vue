<template>
    <div class="flex flex-col h-screen p-5">
        <NavBar v-if="authStore.isAuthenticated"></NavBar>
        <div class="flex-1 w-full">
            <RouterView/>
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
