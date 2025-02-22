<template>
    <main class="h-full flex flex-col items-center">
        <div class="flex just w-full justify-star pt-2 pl-2">
            <router-link :to="{ name: 'welcomePage' }">
                <Button icon="pi pi-chevron-left" variant="text" rounded aria-label="Go back" size="large">
                </Button>
            </router-link>
        </div>
        <div class="flex justify-between items-center pt-5">
            <div class="flex just w-full justify-center">
                <h1 class="text-myGreenPrimary font-bold text-4xl md:text-6xl">
                    LOGIN
                </h1>
            </div>
        </div>
        <section class="w-fit">
            <LoginForm></LoginForm>
        </section>
    </main>
</template>

<script setup>
    import Button from "primevue/button";
    import LoginForm from '@/features/login/components/LoginForm.vue'
</script>

<style scoped>
</style>
