import mitt from 'mitt';
import type { ToastMessageOptions } from 'primevue/toast'

const emitter = mitt();

export interface Confirm {
    title: string,
    message: string,
    closed: (result: boolean) => void
}

export default {
    confirm(confirm: Confirm) {
        emitter.emit('confirm', confirm)
    },
    onConfirm(handler: (message: Confirm) => void) {
        emitter.on('confirm', (message: any) => {
            handler(message)
        })
    },
    showToastMessage(options: ToastMessageOptions) {
        emitter.emit('toast', options)
    },

    onToastMessge(handler: (message: ToastMessageOptions) => void) {
        emitter.on('toast', (message: any) => {
            handler(message)
        })
    },
};