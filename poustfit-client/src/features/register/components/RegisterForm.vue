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

        <div class="flex justify-center items-center">
            <Button label="Iniciar sesión" @click.prevent="login"></Button>
        </div>
    </form>
</template>

<script setup>
    import { ref } from 'vue';
    import { useAuthStore } from '@/stores/authStore'
    import { InputText, Password, Button } from 'primevue'
    import router from '@/router'

    const authStore = useAuthStore();

    const username= ref('');
    const password = ref('');

    const thereIsError = ref(false);
    const errorMessage = ref('');

    const enterPressed = ref(false);

    async function login() {

        if(enterPressed.value) {
            return;
        }

        enterPressed.value = true;

        if (username.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Username está vacío";
            enterPressed.value = false;
            return;
        }

        if (password.value.length < 4) {
            thereIsError.value = true;
            errorMessage.value = "Contraseña tiene que tener como mínimo 4 caracteres";
            enterPressed.value = false;
            return;
        }

        try {
            await authStore.login(
                {
                    username: username.value,
                    password: password.value,
                }
            );

            enterPressed.value = false;

            await router.push("/home");
        } catch (error) {
            if (error) {
                if (error.status === 400) {
                    console.log("Es un bad request")
                }
                if (error.status === 401) {
                    thereIsError.value = true;
                    errorMessage.value = "Username o Contraseña incorrecto";
                }
            }

            enterPressed.value = false;
        }
    }
</script>

<style scoped></style>
