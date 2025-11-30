
import { Api } from "./openapi";
import { useAuthStore } from "../stores/auth";
import router from "../router/router";
import emitter from '../utils/eventBus'

export default function useApi() {
    const authStore = useAuthStore()
    const generatedApi = new Api({ baseURL: '/' });
    const axiosInstance = generatedApi.instance;

    axiosInstance.interceptors.request.use(
        (config) => {

            const token = authStore.accessToken
            if (token) {
                config.headers.Authorization = `Bearer ${token}`
            }
            return config
        },
        (error) => Promise.reject(error)
    )

    axiosInstance.interceptors.response.use(
        config => {
            if (authStore.accessToken) {
                config.headers.authorization = `Bearer ${authStore.accessToken}`
            }

            return config
        },
        async error => {
            const message = error.response.data?.message as string;
            if (error.response.status === 401 && message != null && message.includes('JWT expired')) {
                if (authStore.refreshToken) {
                    const response = await generatedApi.api.refreshAccessToken({ refreshToken: authStore.refreshToken })
                    if (!response || response.status != 200) {
                        router.push('/signin')
                        return
                    }
                    authStore.accessToken = response.data.accessToken
                    authStore.accessToken = response.data.refreshToken
                    return axiosInstance.request(error.config)
                }
            }

            if (error.response.status === 401) {
                router.push('/signin')
                return error.response
            }

            emitter.showToastMessage({ severity: 'error', summary: 'Api error: ' + error.message, life: 3000 });
            return error.response
        }
    )
    return generatedApi.api
}