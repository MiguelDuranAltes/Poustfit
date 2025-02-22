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
                    REGISTER
                </h1>
            </div>
        </div>
        <section class="w-fit">
            <RegisterForm></RegisterForm>
        </section>
    </main>
</template>

<script setup>
    import Button from "primevue/button";
    import RegisterForm from '@/features/register/components/RegisterForm.vue'
</script>

<style scoped>
</style>
