<template>
    <main class="max-h-full overflow-y-scroll flex flex-col items-center md:px-50">
        <ProgressSpinner v-if="isLoading"></ProgressSpinner>
        <div v-else class="flex flex-col gap-2">
            <AccountInfoComp :userInfo="userInfo"></AccountInfoComp>
            <MyPostsComp></MyPostsComp>
        </div>
    </main>
</template>

<script setup>
    import { onMounted, ref } from 'vue'
    import { ProgressSpinner } from 'primevue'
    import errorHandler from '@/communications_backend/common/errorHandler.js'
    import AccountRepository from '@/communications_backend/repository/AccountRepository.js'
    import AccountInfoComp from '@/features/my-profile/components/AccountInfoComp.vue'
    import MyPostsComp from '@/features/my-profile/components/MyPostsComp.vue'

    const isLoading = ref(true);
    const userInfo = ref(null);

    onMounted(async () => {
        isLoading.value = true;

        try {
            userInfo.value = await AccountRepository.getAccountInfo();
            isLoading.value = false;
        } catch (error) {
            errorHandler(error);
        }
    });
</script>

<style scoped>
</style>
