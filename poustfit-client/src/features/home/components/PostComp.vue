
<template>
    <Card class="w-full">
        <template #header>
            <img alt="user header" src="/src/assets/logo.svg" class="w-full"/>
        </template>
        <template #content>
            <div>
                <h2 class="text-xl"> {{ post.autor }} </h2>
            </div>

            <div>
                <p> {{ post.title }} </p>
            </div>

            <div>
                <p class="italic"> {{ fechaHoraInicioFormateada }} </p>
            </div>

            <div class="flex justify-end">
                <Button v-if="usuarioLoTieneEnFavoritos" icon="pi pi-heart-fill"
                        variant="text" rounded aria-label="Like" size="large"
                        @click="dislikePost">
                </Button>

                <Button v-else icon="pi pi-heart" variant="text" rounded aria-label="Dislike"
                        size="large"
                        @click="likePost">
                </Button>
            </div>
        </template>
    </Card>
</template>

<script setup>
    import { Card, Button } from 'primevue'
    import { computed, onMounted, ref } from 'vue'
    import errorHandler from '@/communications_backend/common/errorHandler.js'
    import PostRepository from '@/communications_backend/repository/PostRepository.js'

    const props = defineProps(['post'])

    const usuarioLoTieneEnFavoritos = ref(false);

    onMounted(() => {
        usuarioLoTieneEnFavoritos.value = props.post.usuarioLoTieneEnFavoritos;
    })

    const fechaHoraInicioFormateada = computed(() => {
        const fecha = new Date(props.post.fechaPublicacion);

        return new Intl.DateTimeFormat(navigator.language, {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: "2-digit",
            minute: "2-digit",
            hour12: false, // Asegura formato 24h en regiones que lo usen
        }).format(fecha);
    });

    async function likePost() {
        try {
            await PostRepository.likePost(props.post.id);
            usuarioLoTieneEnFavoritos.value = true;
        } catch (error) {
            errorHandler(error);
        }
    }

    async function dislikePost() {
        try {
            await PostRepository.disLikePost(props.post.id);
            usuarioLoTieneEnFavoritos.value = false;
            emit('dislikePost', props.post.id);
        } catch (error) {
            errorHandler(error);
        }
    }

    const emit = defineEmits(['dislikePost'])
</script>

<style scoped>

</style>
