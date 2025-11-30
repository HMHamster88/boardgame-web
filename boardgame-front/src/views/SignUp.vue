<template>
    <div class="card flex justify-center">
        <Form v-slot="$form" :initialValues :resolver :validateOnValueUpdate="false" :validateOnBlur="true"
            :validateOnMount="['firstName']" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
            <div class="flex flex-col gap-1">
                <InputText name="username" type="text" placeholder="Username" fluid />
                <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{
                    $form.username.error.message }}</Message>
            </div>
            <div class="flex flex-col gap-1">
                <Password name="password" type="text" placeholder="Password" fluid :feedback="false"
                    :formControl="{ validateOnValueUpdate: true }" />
                <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">{{
                    $form.password.error.message }}</Message>
            </div>
            <div class="flex flex-col gap-1">
                <Password name="confirmPassword" type="text" placeholder="Confirm Password" fluid :feedback="false"
                    :formControl="{ validateOnValueUpdate: true }" />
                <Message v-if="$form.confirmPassword?.invalid" severity="error" size="small" variant="simple">{{
                    $form.confirmPassword.error.message }}</Message>
            </div>
            <Button type="submit" severity="secondary" label="Submit" />
        </Form>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import useAuthService from '../services/authService';
import { useRouter } from 'vue-router';
import { z } from 'zod'
import { zodResolver } from '@primevue/forms/resolvers/zod';

const authService = useAuthService()
const router = useRouter()

const schema = z.object({
    username: z.string().min(1, 'Name is required'),
    password: z.string().min(1, 'Password is required'),
    confirmPassword: z.string().min(1, 'Cobfirm password is required'),
}).superRefine(({ confirmPassword, password }, ctx) => {
    if (confirmPassword !== password) {
        ctx.addIssue({
            code: "custom",
            message: "The passwords did not match",
            path: ['confirmPassword']
        });
    }
});

interface LoginInfo {
    username: string,
    password: string,
    confirmPassword: string
}

const initialValues = ref<LoginInfo>({
    username: '',
    password: '',
    confirmPassword: ''
});

const resolver = zodResolver(schema)

const onFormSubmit = async (formData: FormData) => {
    const values = formData as any;
    if (values.valid) {
        const result = await authService.sighUp(values.states.username.value, values.states.password.value)
        if (result) {
            router.push('/')
        }
    }
}
</script>