<script setup>
import { ref, onMounted } from 'vue'
import { getMyInfo, updateMyInfo } from '@/api/delivery'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const loading = ref(false)
const saving = ref(false)
const editing = ref(false)
const toast = ref('')
const employee = ref(null)

const form = ref({ username: '', password: '' })

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadProfile() {
  loading.value = true
  try {
    employee.value = await getMyInfo()
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function startEdit() {
  form.value = { username: employee.value.username || '', password: '' }
  editing.value = true
}

function cancelEdit() {
  editing.value = false
  form.value = { username: '', password: '' }
}

async function handleSave() {
  if (!form.value.username) {
    showToast('请输入用户名')
    return
  }
  saving.value = true
  try {
    await updateMyInfo({
      id: employee.value.id,
      username: form.value.username,
      password: form.value.password || undefined,
    })
    showToast('修改成功')
    editing.value = false
    // 更新 auth store 中的用户名
    if (auth.userInfo) {
      auth.userInfo.username = form.value.username
      localStorage.setItem('empInfo', JSON.stringify(auth.userInfo))
    }
    await loadProfile()
  } catch (e) {
    showToast(e.message || '修改失败')
  } finally {
    saving.value = false
  }
}

onMounted(loadProfile)
</script>

<template>
  <div class="profile">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="employee" class="profile-card">
      <!-- 查看模式 -->
      <div v-if="!editing" class="info-display">
        <div class="info-row">
          <span class="label">姓名</span>
          <span class="value">{{ employee.name }}</span>
        </div>
        <div class="info-row">
          <span class="label">登录账号</span>
          <span class="value">{{ employee.username }}</span>
        </div>
        <div class="info-row">
          <span class="label">岗位</span>
          <span class="value">{{ employee.job === 0 ? '管理员' : '配送员' }}</span>
        </div>
        <div class="info-row">
          <span class="label">状态</span>
          <span class="value">{{ employee.stage === 0 ? '空闲' : '配送中' }}</span>
        </div>
        <div class="info-row">
          <span class="label">入职日期</span>
          <span class="value">{{ employee.entryTime || '--' }}</span>
        </div>
        <div class="info-row">
          <span class="label">员工ID</span>
          <span class="value">{{ employee.id }}</span>
        </div>
        <div class="actions">
          <button class="btn-primary" @click="startEdit">修改信息</button>
        </div>
      </div>

      <!-- 编辑模式 -->
      <div v-else class="info-edit">
        <div class="form-group">
          <label>姓名</label>
          <input :value="employee.name" disabled />
        </div>
        <div class="form-group">
          <label>登录账号</label>
          <input v-model="form.username" placeholder="输入新用户名" />
        </div>
        <div class="form-group">
          <label>新密码（留空不修改）</label>
          <input v-model="form.password" type="password" placeholder="输入新密码" />
        </div>
        <div class="actions">
          <button class="btn-outline" @click="cancelEdit">取消</button>
          <button class="btn-primary" :disabled="saving" @click="handleSave">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.profile { max-width: 600px; }

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  color: #e0e0e0;
  font-weight: 600;
}

.loading {
  text-align: center;
  color: #666;
  padding: 60px 0;
  font-size: 14px;
}

.profile-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 28px 32px;
}

.info-display .info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #2a2a2a;
}

.info-row:last-of-type {
  border-bottom: none;
}

.info-row .label {
  font-size: 13px;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-row .value {
  font-size: 14px;
  color: #e0e0e0;
}

.actions {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
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

.form-group input:disabled {
  opacity: 0.4;
  cursor: not-allowed;
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

.btn-primary:hover:not(:disabled) { background: #c49640; }

.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }

.btn-outline {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
}

.btn-outline:hover { color: #d4a853; border-color: #d4a853; }

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