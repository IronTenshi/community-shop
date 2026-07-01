<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { empLogin } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const form = ref({ username: '', password: '' })
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''
  if (!form.value.username || !form.value.password) {
    error.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  try {
    const data = await empLogin(form.value)
    auth.setAuth(data)
    router.push('/delivery/orders')
  } catch (e) {
    error.value = e.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <h1>community-employee-login</h1>
      <p class="subtitle">配送员 / 管理员 登录</p>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="form.username" placeholder="输入用户名" @keyup.enter="handleLogin" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="form.password" type="password" placeholder="输入密码" @keyup.enter="handleLogin" />
      </div>
      <p v-if="error" class="error-msg">{{ error }}</p>
      <button class="btn-primary" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登 录' }}
      </button>
    </div>
  </div>
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { background: #0d0d0d; font-family: 'Segoe UI', sans-serif; }
</style>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #0d0d0d;
}

.login-card {
  width: 380px;
  padding: 40px 36px;
  background: #1a1a1a;
  border: 1px solid #2a2a2a;
}

.login-card h1 {
  font-size: 22px;
  color: #d4a853;
  text-align: center;
  margin-bottom: 6px;
}

.subtitle {
  font-size: 13px;
  color: #555;
  text-align: center;
  margin-bottom: 28px;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  font-size: 12px;
  color: #777;
  margin-bottom: 6px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  background: #141414;
  border: 1px solid #2a2a2a;
  color: #e0e0e0;
  font-size: 14px;
  outline: none;
}

.form-group input:focus {
  border-color: #d4a853;
}

.error-msg {
  color: #c0392b;
  font-size: 13px;
  margin-bottom: 12px;
  text-align: center;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background: #d4a853;
  color: #141414;
  border: 1px solid #d4a853;
  font-size: 14px;
  cursor: pointer;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: all 0.15s;
}

.btn-primary:hover:not(:disabled) {
  background: #c49640;
  border-color: #c49640;
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>