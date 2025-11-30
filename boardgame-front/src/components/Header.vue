<template>
    <Menubar>
        <template #start>
            <Button label="Games" class="mr-2" as="router-link" variant="link" to="/games" />
        </template>


        <template #end>
            <div>
                <Button label="Sign In" class="mr-2" as="router-link" to="/signin" v-if="!authStore.user" />
                <Button label="Sign Up" class="mr-2" as="router-link" to="/signup" v-if="!authStore.user" />
                <Button label="Logout" class="mr-2" text v-if="authStore.user" v-on:click="logut" />
            </div>
        </template>
    </Menubar>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const authStore = useAuthStore()
const authService = useAuthStore()
const router = useRouter()

function logut() {
    authService.clearAuth()
    router.push('/')
}
</script>