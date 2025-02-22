<template>
    <form class="max-w-xs p-5 flex flex-col gap-4 min-w-80">
        <div class="flex flex-col gap-2">
            <label for="username">Username</label>
            <InputText :id="'username'" placeholder="Username" v-model="username" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="password">Password</label>
            <Password placeholder="Password" v-model="password" :feedback="false" toggleMask :inputClass="'w-full'"/>
        </div>

        <Message v-if="thereIsError" severity="error">{{ errorMessage }}</Message>

        <div class="flex justify-center items-center">
            <Button label="Iniciar sesión" @click.prevent="login"></Button>
        </div>
    </form>
</template>

<script setup>
    import { ref } from 'vue';
    import { useAuthStore } from '@/stores/authStore'
    import { InputText, Password, Button, Message } from 'primevue'
    import router from '@/router'

    const authStore = useAuthStore();

    const username= ref('');
    const password = ref('');

    const thereIsError = ref(false);
    const errorMessage = ref('');

    async function login() {

        if (username.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Username is empty";
            return;
        }

        if (password.value.length <= 0) {
            thereIsError.value = true;
            errorMessage.value = "Password is empty";
            return;
        }

        try {
            await authStore.login(
                {
                    username: username.value,
                    password: password.value,
                }
            );

            await router.push("/home");
        } catch (error) {
            if (error.status === 401) {
                thereIsError.value = true;
                errorMessage.value = "Username or Password is incorrect";
            }
        }
    }
</script>

<style scoped></style>
