import { createApp } from 'vue'
import 'primeicons/primeicons.css';
import './styles.css'
import App from './App.vue'
import PrimeVue from 'primevue/config'
import ToastService from 'primevue/toastservice';
import ConfirmationService from 'primevue/confirmationservice'
import DialogService from 'primevue/dialogservice'
import Aura from '@primevue/themes/aura'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import Tooltip from 'primevue/tooltip';

import router from './router/router'

export const app = createApp(App)

app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'system',
            cssLayer: false
        }
    }
})

app.directive('tooltip', Tooltip);
app.use(router)
app.use(ToastService);
app.use(ConfirmationService)
app.use(DialogService)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

app.mount('#app')
