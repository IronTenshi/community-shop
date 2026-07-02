import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  base : '/user/',
  server: {
    proxy: {
      // 将 /user/login、/shops、/users 等 API 请求转发到后端
      // 注意：/user 前端页面路径不代理，只代理 API 路径
      '^/(user/(login|register|me)|user/\\d+|shops|shopss|users|productss)': {
        target: 'http://localhost:8090',
        changeOrigin: true,
      }
    }
  }
})
