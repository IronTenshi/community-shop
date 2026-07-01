<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { userLogin } from '@/api/auth'

const router = useRouter()
const auth = useAuthStore()

const form = ref({ username: '', password: '' })
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  error.value = ''
  if (!form.value.username || !form.value.password) {
    error.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  try {
    const data = await userLogin(form.value)
    console.log('[Login] 登录响应数据:', JSON.stringify(data))
    console.log('[Login] token:', data.token ? data.token.substring(0, 30) + '...' : 'null')
    auth.setAuth(data)
    console.log('[Login] token已存入localStorage:', localStorage.getItem('token')?.substring(0, 30) + '...')
    router.push('/products')
  } catch (e) {
    console.error('[Login] 登录失败:', e)
    error.value = e.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <h1>community-shop</h1>
      <p class="subtitle">用户登录</p>

      <div class="form-item">
        <label>用户名</label>
        <input
          v-model="form.username"
          placeholder="输入用户名"
          @keyup.enter="handleLogin"
        />
      </div>

      <div class="form-item">
        <label>密码</label>
        <input
          v-model="form.password"
          type="password"
          placeholder="输入密码"
          @keyup.enter="handleLogin"
        />
      </div>

      <p v-if="error" class="error-msg">{{ error }}</p>

      <button class="submit-btn" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登 录' }}
      </button>

      <p class="footer-text">
        没有账号？<router-link to="/register">立即注册</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a1a1a;
}

.login-card {
  width: 100%;
  max-width: 380px;
  padding: 44px 36px;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.login-card h1 {
  text-align: center;
  font-size: 24px;
  color: #d4a853;
  font-weight: 600;
  letter-spacing: 2px;
  margin-bottom: 4px;
}

.subtitle {
  text-align: center;
  color: #666;
  font-size: 13px;
  margin-bottom: 32px;
  letter-spacing: 1px;
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.form-item input {
  width: 100%;
  padding: 10px 12px;
  background: #141414;
  border: 1px solid #333;
  color: #e0e0e0;
  font-size: 14px;
  outline: none;
  transition: border-color 0.15s;
}

.form-item input:focus {
  border-color: #d4a853;
}

.form-item input::placeholder {
  color: #555;
}

.error-msg {
  color: #c0392b;
  font-size: 13px;
  margin-bottom: 16px;
  text-align: center;
}

.submit-btn {
  width: 100%;
  padding: 11px 0;
  background: #d4a853;
  color: #1a1a1a;
  border: none;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 2px;
  cursor: pointer;
  transition: background 0.15s;
  text-transform: uppercase;
}

.submit-btn:hover:not(:disabled) {
  background: #c49a3c;
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.footer-text {
  text-align: center;
  margin-top: 20px;
  font-size: 13px;
  color: #666;
}

.footer-text a {
  color: #d4a853;
  text-decoration: none;
}

.footer-text a:hover {
  text-decoration: underline;
}
</style>