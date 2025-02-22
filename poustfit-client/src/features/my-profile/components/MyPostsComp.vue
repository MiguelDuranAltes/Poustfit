<template>
    <ul class="flex flex-wrap justify-between gap-5 pb-10 w-full pr-7 pl-7
               md:pr-[90px] md:pl-[90px]"
        @scroll="handleScroll"
    >
        <li v-for="post in resultList" :key="post.id">
            <PostComp :post="post"></PostComp>
        </li>

        <ProgressSpinner v-if="isLoading"></ProgressSpinner>
    </ul>
    <p v-if="!isLoading && resultList.length === 0">
        You have no outfits yet! Publish one!
    </p>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
    import { ProgressSpinner } from 'primevue'
    import errorHandler from '@/communications_backend/common/errorHandler.js'
    import PostComp from '@/features/home/components/PostComp.vue'
    import AccountRepository from '@/communications_backend/repository/AccountRepository.js'

    const isLoading = ref(false);
    const resultPage = ref(null);
    const resultList = ref([]);

    const currentPage = ref(0); // Página actual
    const pageSize = ref(2); // Tamaño de página, nunca cambia
    const totalPages = ref(1); // Total de páginas

    let mainElement = null;

    onMounted(async () => {

        await nextTick(() => {
            mainElement = document.querySelector("main");
            if (mainElement) {
                mainElement.addEventListener("scroll", handleScroll);
            }
        });
        await fetchPosts();
    })

    async function fetchPosts() {

        if (isLoading.value || currentPage.value >= totalPages.value) return;

        try {
            isLoading.value = true;
            resultPage.value = await AccountRepository.getMyPosts(currentPage.value, pageSize.value);

            if (resultPage.value.content.length === 0) {
                isLoading.value = false;
                return;
            }

            for (const post of resultPage.value.content) {
                resultList.value.push({
                    ...post
                });
            }

            totalPages.value = resultPage.value.totalPages;
            currentPage.value++;
            isLoading.value = false;
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
            await fetchPosts();
        }
    }
</script>

<style scoped>

</style>
