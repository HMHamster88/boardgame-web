import { useAuthStore } from "../stores/auth"
import useApi from "../api/api"

export default function useAuthService() {
    const api = useApi()
    const authStore = useAuthStore()

    return {
        async signIn(username: string, password: string): Promise<boolean> {

            const response = (await api.signIn({ username: username, password: password }))

            if (response.status != 200) {
                return false
            }

            const data = response.data

            authStore.setTokens(data.accessToken, data.refreshToken)
            authStore.setUser({ name: username, id: data.user.id || '' })
            return true
        },

        async logout() {
            authStore.clearAuth()
        },

        async sighUp(username: string, password: string) {
            const response = (await api.signUp({ username: username, password: password }))
            if (response.status != 200) {
                return false
            }
            const data = response.data
            authStore.setTokens(data.accessToken, data.refreshToken)
            authStore.setUser({ name: username, id: data.user.id || '' })
            return true
        }
    }
}