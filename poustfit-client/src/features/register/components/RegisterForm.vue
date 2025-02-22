<template>
    <form class="max-w-xs p-5 flex flex-col gap-4 min-w-80">
        <div>
            <Button label="Choose image" @click.prevent="iniciarSubidaFichero()">
            </Button>
        </div>

        <input ref="inputOculto" class="hidden" type="file" @change="actualizarImagen()" />

        <div class="w-[150px]">
            <img
                v-if="!auxName && imageSrc"
                :src="imageSrc"
                class="w-[120px] h-[120px] rounded-full"
                alt="Outfit photo"
            />
            <img
                v-if="auxName"
                :src="previewImageSrc"
                class="w-[120px] h-[120px] rounded-full"
                alt="Outfit photo"
            />
            <p v-if="!auxName && !imageSrc" class="mt-5">Image not selected</p>
        </div>

        <div class="flex flex-col gap-2">
            <label for="username">Username</label>
            <InputText :id="'username'" placeholder="Username" v-model="username" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="nombre">Name</label>
            <InputText id="nombre" placeholder="Name" v-model="nombre" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="apellidos">Lastname</label>
            <InputText id="apellidos" placeholder="Lastname" v-model="apellidos" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="correo">Email</label>
            <InputText id="correo" type="email" placeholder="Email" v-model="correo" required />
        </div>

        <div class="flex flex-col gap-2">
            <label for="descripcion">Description</label>
            <Textarea id="descripcion" placeholder="This is a description..." v-model="descripcion" autoResize rows="4"/>
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
    import { ref, useTemplateRef } from 'vue'
    import { useAuthStore } from '@/stores/authStore'
    import { InputText, Password, Button, Textarea, Message } from 'primevue'
    import router from '@/router'
    import AccountRepository from '@/communications_backend/repository/AccountRepository.js'

    const authStore = useAuthStore();

    const username= ref('');
    const password = ref('');
    const repeatPassword = ref('');
    const nombre = ref('');
    const apellidos = ref('');
    const correo = ref('');
    const descripcion = ref('');

    const auxName = ref("");
    const imageSrc = ref("");
    const previewImageSrc = ref("");
    const inputOculto = useTemplateRef('inputOculto');

    const thereIsError = ref(false);
    const errorMessage = ref('');

    function iniciarSubidaFichero() {
        inputOculto.value.click();
    }

    function actualizarImagen() {
        console.log("actualizarImagen");
        auxName.value = inputOculto.value.files[0].name;
        previewImageSrc.value = URL.createObjectURL(inputOculto.value.files[0]);
    }

    async function register() {

        if(inputOculto.value.files[0] == null) {
            thereIsError.value = true;
            errorMessage.value = "Image is necessary";
            return;
        }

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

            if(inputOculto.value.files[0] != null) {
                await AccountRepository.saveImage(inputOculto.value.files[0]);
            }

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
