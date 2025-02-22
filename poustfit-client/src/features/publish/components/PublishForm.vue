<template>
    <form class="w-full border-2 border-myGreenPrimary rounded-2xl p-5 flex flex-col gap-4 min-w-80">
        <div class="flex flex-col gap-4">
            <div class="flex flex-col gap-2">
                <label for="title">Title</label>
                <InputText id="title" placeholder="Title" v-model="title" required />
            </div>

            <label for="outfitPhoto" class="text-darkText font-bold">
                Outfit photo
            </label>

            <div>
                <Button label="Choose image" @click.prevent="iniciarSubidaFichero()">
                </Button>
            </div>

            <input ref="inputOculto" class="hidden" type="file" @change="actualizarImagen()" />

            <div class="w-[300px]">
                <img
                    v-if="!auxName && imageSrc"
                    :src="imageSrc"
                    class="img-fluid mt-3 mb-3"
                    alt="Outfit photo"
                />
                <img
                    v-if="auxName"
                    :src="previewImageSrc"
                    class="img-fluid mt-3 mb-3"
                    alt="Outfit photo"
                />
                <p v-if="!auxName && !imageSrc" class="mt-5">Image not selected</p>
            </div>
        </div>

        <Message v-if="thereIsError" severity="error">{{ errorMessage }}</Message>

        <div class="flex justify-center items-center">
            <Button label="Publish" @click.prevent="publish">
            </Button>
        </div>
    </form>

</template>

<script setup>
    import Button from 'primevue/button'
    import { ref, useTemplateRef } from 'vue'
    import { InputText, Message } from 'primevue'
    import router from '@/router/index.js'
    import PostRepository from '@/communications_backend/repository/PostRepository.js'
    import Swal from 'sweetalert2'
    import errorHandler from '@/communications_backend/common/errorHandler.js'

    const auxName = ref("");
    const imageSrc = ref("");
    const previewImageSrc = ref("");

    const title = ref("");

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

    async function publish () {
        if (title.value.length === 0) {
            thereIsError.value = true;
            errorMessage.value = "Title is empty";
            return;
        }

        if(inputOculto.value.files[0] == null) {
            thereIsError.value = true;
            errorMessage.value = "Image is necessary";
            return;
        }

        try {
            const savedPost = await PostRepository.createPost({
                title: title.value,
            });

            if(inputOculto.value.files[0] != null) {
                await PostRepository.saveImage(savedPost.id, inputOculto.value.files[0]);
            }

            await router.push("/home");

            Swal.fire({
                title: 'Outfit posted!',
                text: 'Outfit posted successfully',
                icon: 'success',
            });
        } catch (error) {
            errorHandler(error);
        }
    }

</script>

<style scoped></style>
