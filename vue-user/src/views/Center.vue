<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { updateUser } from '@/api/auth'

const auth = useAuthStore()
const showModal = ref(false)
const loading = ref(false)
const toast = ref('')

const form = ref({
  username: auth.userInfo?.username || '',
  password: '',
  phone: auth.userInfo?.phone || '',
  address: auth.userInfo?.address || '',
})

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

function openEdit() {
  form.value = {
    username: auth.userInfo?.username || '',
    password: '',
    phone: auth.userInfo?.phone || '',
    address: auth.userInfo?.address || '',
  }
  showModal.value = true
}

async function handleSave() {
  if (!form.value.username) {
    showToast('用户名不能为空')
    return
  }
  loading.value = true
  try {
    await updateUser(form.value)
    // 更新 store 中的 userInfo
    await auth.refreshUserInfo()
    showToast('修改成功')
    showModal.value = false
  } catch (e) {
    showToast(e.message || '修改失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  auth.refreshUserInfo()
})
</script>

<template>
  <div class="center-page">
    <h2 class="page-title">用户中心</h2>

    <div class="info-card">
      <div class="info-row">
        <span class="label">用户名</span>
        <span class="value">{{ auth.username || '-' }}</span>
      </div>
      <div class="info-row">
        <span class="label">用户 ID</span>
        <span class="value">{{ auth.userInfo?.id || '-' }}</span>
      </div>
      <div class="info-row">
        <span class="label">手机号</span>
        <span class="value">{{ auth.userInfo?.phone || '-' }}</span>
      </div>
      <div class="info-row">
        <span class="label">收货地址</span>
        <span class="value">{{ auth.userInfo?.address || '-' }}</span>
      </div>
      <div class="actions">
        <button class="btn-primary" @click="openEdit">修改信息</button>
      </div>
    </div>

    <!-- 修改信息弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h3>修改个人信息</h3>
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" placeholder="输入用户名" />
        </div>
        <div class="form-group">
          <label>新密码（留空不修改）</label>
          <input v-model="form.password" type="password" placeholder="输入新密码" />
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input v-model="form.phone" placeholder="输入手机号" />
        </div>
        <div class="form-group">
          <label>收货地址</label>
          <input v-model="form.address" placeholder="输入收货地址" />
        </div>
        <div class="modal-actions">
          <button class="btn-outline" @click="showModal = false">取消</button>
          <button class="btn-primary" @click="handleSave" :disabled="loading">
            {{ loading ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.center-page {
}

.page-title {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 500;
  letter-spacing: 1px;
  margin-bottom: 24px;
}

.info-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 24px;
}

.info-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #222;
  font-size: 14px;
}

.info-row:last-of-type {
  border-bottom: none;
}

.label {
  color: #666;
  width: 100px;
  flex-shrink: 0;
}

.value {
  color: #ccc;
}

.actions {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #222;
  text-align: right;
}

/* 弹窗 */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 28px 32px;
  width: 420px;
}

.modal h3 {
  font-size: 18px;
  color: #e0e0e0;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 13px;
  color: #888;
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
  box-sizing: border-box;
}

.form-group input:focus {
  border-color: #d4a853;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 24px;
}

.btn-primary {
  padding: 8px 20px;
  background: #d4a853;
  color: #141414;
  border: 1px solid #d4a853;
  font-size: 13px;
  cursor: pointer;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.btn-primary:hover:not(:disabled) {
  background: #c49640;
  border-color: #c49640;
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-outline {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
}

.btn-outline:hover {
  color: #d4a853;
  border-color: #d4a853;
}

.toast {
  position: fixed;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  background: #2a2a2a;
  color: #e0e0e0;
  border: 1px solid #444;
  padding: 10px 24px;
  font-size: 13px;
  z-index: 200;
}
</style>