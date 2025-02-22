<template>
    <main class="min-h-full flex flex-col gap-10 items-center justify-center">
        <section class="text-center">
            <h1 class="text-myGreenPrimary font-bold text-5xl md:text-8xl">
                WELCOME TO <span class="font-bold italic">POUSTFIT</span>
            </h1>
            <h3 class="text-base mt-9 md:text-3xl">Log in or create an account to enjoy!</h3>
        </section>
        <section class="flex justify-center gap-10">
            <router-link :to="{name: 'login'}">
                <Button label="Login">
                </Button>
            </router-link>
            <router-link :to="{name: 'register'}">
                <Button label="Register">
                </Button>
            </router-link>
        </section>
    </main>
</template>

<script setup>
    import { Button } from 'primevue'
</script>

<style scoped>

</style>
