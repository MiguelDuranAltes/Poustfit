<template>
    <Menubar :model="items">
        <template #item="{ item, props }">
            <router-link v-slot="{ href, navigate, isActive }" :to="item.route" custom>
                <a :href="href" v-bind="props.action" @click="navigate">
                    <span :class="item.icon" />
                    <span :class="{ 'text-bold': isActive }">{{ item.label }}</span>
                </a>
            </router-link>
        </template>
    </Menubar>
</template>

<script setup>
    import  Menubar from 'primevue/menubar';
    import { ref } from 'vue'

    const items = ref([
        {
            label: 'Home',
            icon: 'pi pi-home',
            route: '/home'
        },
        {
            label: 'My profile',
            icon: 'pi pi-user',
            route: '/my-profile'
        }
    ]);
</script>

<style scoped>

</style>
