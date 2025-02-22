<template>
    <Card class="w-full">
        <template #content>
            <div class="flex w-full gap-1">
                <div class="rounded-full border-2 border-myGreenPrimary shrink-0">
                    <img :src="'/src/assets/logo.svg'" alt="Profile photo" class="w-[120px] h-[120px] rounded-full">
                </div>

                <div class="flex flex-col flex-grow text-center w-[133px]">
                    <h2 class="text-myGreenPrimary font-bold text-3xl overflow-hidden text-ellipsis"> {{ userInfo.username }} </h2>
                </div>
            </div>

            <div class="flex flex-col w-full gap-3 pt-3">
                <div class="flex flex-col w-full gap-3 pt-3">
                    <div class="flex flex-col gap-1">
                        <h2 class="text-myGreenPrimary font-bold text-xl"> {{ fullName }} </h2>
                        <p v-html="userInfo.descripcion" class="text-myGreenPrimary max-h-20 overflow-y-scroll"></p>
                    </div>
                </div>
            </div>
        </template>
    </Card>
</template>

<script setup>
    import { Card } from 'primevue'
    import { onMounted, ref } from 'vue'

    const props = defineProps(['userInfo'])

    const fullName = ref("");

    onMounted(() => {
        fullName.value = props.userInfo.nombre + " " + props.userInfo.apellidos;
    })
</script>

<style scoped>

</style>
