<template>
    <main class="h-full flex flex-col items-center">
        <div v-if="!isLoading" class="flex flex-col h-full w-full gap-5 overflow-y-scroll sm:flex-row sm:overflow-hidden" @scroll="handleScroll">
            <div class="md:w-[280px]">
                <PostDetailsComp :post="post"></PostDetailsComp>
            </div>
            <div class="grow h-full md:overflow-y-scroll" @scroll="handleScroll">
                <ul>
                    <li v-for="recommendation in recommendationsList" :key="recommendation.id">

                        <!--
                        <a :href="recommendation.link" target="_blank" class="hover:text-emerald-600">{{ recommendation.name }}</a>
                        -->
                        <h2 class="hover:text-emerald-600 hover:cursor-pointer" @click="getProductPhoto(recommendation.name, recommendation.link)"> {{ recommendation.name }} </h2>
                    </li>
                    <ProgressSpinner v-if="isLoadingLocal"></ProgressSpinner>
                </ul>
            </div>
            <div v-if="showingImage" class="flex grow justify-center items-center">
                <ProgressSpinner v-if="isLoadingProductImage"></ProgressSpinner>
                <a v-else :href="productUrl" target="_blank">
                    <img :src="productImageSrc" alt="Product image" >
                </a>
            </div>
        </div>
        <ProgressSpinner v-else></ProgressSpinner>
    </main>
</template>

<script setup>
    import PostDetailsComp from '@/features/details/component/PostDetailsComp.vue'
    import { useRoute } from 'vue-router'
    import PostRepository from '@/communications_backend/repository/PostRepository.js'
    import { onMounted, ref } from 'vue'
    import { ProgressSpinner } from 'primevue'
    import errorHandler from '@/communications_backend/common/errorHandler.js'
    import InditexRepository from '@/communications_backend/repository/InditexRepository.js'
    import inditexRepository from '@/communications_backend/repository/InditexRepository.js'

    const route = useRoute();
    const post = ref(null);

    const recommendationsList = ref([]);
    const currentPage = ref(1);
    const pageSize = ref(40);

    const productImageSrc = ref('');

    const isLoading = ref(true);

    const isLoadingLocal = ref(false);

    const isLoadingProductImage = ref(false);
    const showingImage = ref(false);
    const productUrl = ref('');

    onMounted(async () => {
        isLoading.value = true;

        try {
            post.value = await PostRepository.getPost(route.params.postId);

            await fetchInditexRecommendations();

            isLoading.value = false;
        } catch (error) {
            errorHandler(error);
        }
    });

    async function fetchInditexRecommendations() {

        if (isLoadingLocal.value) return;

        try {
            isLoadingLocal.value = true;
            const parcialList = await InditexRepository.getInditexRecommendations(post.value.url_externa, currentPage.value, pageSize.value);

            recommendationsList.value.push(...parcialList);

            currentPage.value++;
            isLoadingLocal.value = false;
        } catch (error) {
            errorHandler(error);
        }
    }

    async function handleScroll(event) {
        const target = event.target;

        // scrollHeight: el alto total del contenido del elemento, es decir, contando el overflow
        // scrollTop: la cantidad de pixeles que se ha hecho scroll (hacia abajo)
        // clientHeight: el alto del elemento, incluyendo padding pero sin contar el borde, y sin contar el overflow

        const bottomReached = target.scrollHeight - target.scrollTop <= target.clientHeight + 10;
        if (bottomReached) {
            console.log("bottom reached")
            await fetchInditexRecommendations();
        }
    }

    async function getProductPhoto(name, url) {
        productUrl.value = url;
        showingImage.value = true;
        isLoadingProductImage.value = true;
        try {
            productImageSrc.value = await inditexRepository.getProductPhoto(name, url);
            isLoadingProductImage.value = false;
        } catch (error) {
            errorHandler(error);
        }
    }

</script>

<style scoped>
</style>
