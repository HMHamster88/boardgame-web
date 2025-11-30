import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import tailwindcss from '@tailwindcss/vite';
import { PrimeVueResolver } from '@primevue/auto-import-resolver';
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    tailwindcss(),
    Components({
      resolvers: [PrimeVueResolver()],
    }),
  ],
  server: {
    host: 'localhost',
    proxy: {
      // Proxying websockets:
      // Requests to ws://localhost:5173/socket.io
      // will be forwarded to ws://localhost:5174/socket.io
      '/api/websocket': {
        target: 'ws://localhost:8080',
        ws: true,
        changeOrigin: true, // often necessary for origin-based security
        rewriteWsOrigin: true
      },
      // You can also proxy other paths with specific targets
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
    },
  },
  resolve: {
    alias: {
      '~': fileURLToPath(new URL('./src', import.meta.url)) // Alias for src folder
    }
  }
})
