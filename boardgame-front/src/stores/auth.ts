import { defineStore } from 'pinia'

interface StoreUser {
    id: string
    name: string
}

interface AuthState {
    user: StoreUser | null
    accessToken: string | null
    refreshToken: string | null
    isAuthenticated: boolean
    loading: boolean
}

export const useAuthStore = defineStore(
    'auth',
    {
        state: (): AuthState => ({
            user: null,
            accessToken: null,
            refreshToken: null,
            isAuthenticated: false,
            loading: true
        }),
        persist: true,
        actions: {
            setUser(user: StoreUser) {
                this.user = user
                this.isAuthenticated = true
            },

            setTokens(accessToken: string, refreshToken: string) {
                this.accessToken = accessToken
                this.refreshToken = refreshToken
            },

            clearAuth() {
                this.user = null
                this.accessToken = null
                this.isAuthenticated = false
            }
        }
    }
)
