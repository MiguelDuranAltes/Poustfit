<template>
    <form class="max-w-xs p-5 flex flex-col gap-4 min-w-80">
        <div class="flex flex-col gap-2">
            <label for="username">Username</label>
            <InputText :id="'username'" placeholder="Username" v-model="username" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="nombre">Nombre</label>
            <InputText id="nombre" placeholder="Nombre" v-model="nombre" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="apellidos">Apellidos</label>
            <InputText id="apellidos" placeholder="Apellidos" v-model="apellidos" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="correo">Correo</label>
            <InputText id="correo" type="email" placeholder="Correo" v-model="correo" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="descripcion">Descripción</label>
            <Textarea id="descripcion" placeholder="Escribe una descripción..." v-model="descripcion" autoResize rows="4"/>
        </div>

        <div class="flex flex-col gap-2">
            <label for="password">Password</label>
            <Password id="password" placeholder="Password" v-model="password" :feedback="false" toggleMask :inputClass="'w-full'"/>
        </div>

        <div class="flex flex-col gap-2">
            <label for="repeat-password">Repetir Password</label>
            <Password placeholder="Repeat password" v-model="repeatPassword" :feedback="false" toggleMask :inputClass="'w-full'"/>
        </div>

        <Message v-if="thereIsError" severity="error">{{ errorMessage }}</Message>

        <div class="flex justify-center items-center">
            <Button label="Register" @click.prevent="register"></Button>
        </div>
    </form>
</template>

<script setup>
    import { ref } from 'vue';
    import { useAuthStore } from '@/stores/authStore'
    import { InputText, Password, Button, Textarea, Message } from 'primevue'
    import router from '@/router'

    const authStore = useAuthStore();

    const username= ref('');
    const password = ref('');
    const repeatPassword = ref('');
    const nombre = ref('');
    const apellidos = ref('');
    const correo = ref('');
    const descripcion = ref('');

    const thereIsError = ref(false);
    const errorMessage = ref('');

    async function register() {

        if (username.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Username is empty";
            return;
        }

        if (nombre.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Name is empty";
            return;
        }

        if (apellidos.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Last name is empty";
            return;
        }

        if (correo.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Email is empty";
            return;
        }

        if (descripcion.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Description is empty";
            return;
        }

        if (password.value.length < 4) {
            thereIsError.value = true;
            errorMessage.value = "Password must be at least 4 characters long";
            return;
        }

        if (password.value !== repeatPassword.value) {
            thereIsError.value = true;
            errorMessage.value = "Passwords do not match";
            return;
        }

        try {
            await authStore.register(
                {
                    nombre: nombre.value,
                    apellidos: apellidos.value,
                    username: username.value,
                    correo: correo.value,
                    password: password.value,
                    descripcion: descripcion.value,
                }
            );

            await router.push("/home");
        } catch (error) {
            if (error.status === 409) {
                thereIsError.value = true;
                errorMessage.value = error.data.message;
            }
            if (error.status === 401) {
                await authStore.logout();
                await router.push("/");
            }
        }
    }
</script>

<style scoped></style>
