<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/auth'

const router = useRouter()

const form = ref({ username: '', password: '', phone: '', address: '' })
const loading = ref(false)
const error = ref('')

async function handleRegister() {
  error.value = ''
  const f = form.value
  if (!f.username || !f.password || !f.phone || !f.address) {
    error.value = '请填写完整信息'
    return
  }
  loading.value = true
  try {
    await userRegister(f)
    router.push('/login')
  } catch (e) {
    error.value = e.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="register-page">
    <div class="register-card">
      <h1>社区商店</h1>
      <p class="subtitle">用户注册</p>

      <div class="form-row">
        <div class="form-item">
          <label>用户名</label>
          <input v-model="form.username" placeholder="输入用户名" />
        </div>
        <div class="form-item">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="输入密码" />
        </div>
      </div>

      <div class="form-item">
        <label>手机号</label>
        <input v-model="form.phone" placeholder="输入手机号" />
      </div>

      <div class="form-item">
        <label>收货地址</label>
        <input v-model="form.address" placeholder="输入收货地址" />
      </div>

      <p v-if="error" class="error-msg">{{ error }}</p>

      <button class="submit-btn" :disabled="loading" @click="handleRegister">
        {{ loading ? '注册中...' : '注 册' }}
      </button>

      <p class="footer-text">
        已有账号？<router-link to="/login">去登录</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a1a1a;
}

.register-card {
  width: 100%;
  max-width: 420px;
  padding: 40px 36px;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.register-card h1 {
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
  margin-bottom: 28px;
  letter-spacing: 1px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-item {
  margin-bottom: 18px;
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