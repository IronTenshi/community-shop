<script setup>
import { ref, onMounted } from 'vue'
import { createEmployee, updateEmployee, deleteEmployee, getEmployee, listEmployees } from '@/api/admin'

const employees = ref([])
const loading = ref(false)
const showModal = ref(false)
const editingEmp = ref(null)
const form = ref({ name: '', username: '', password: '', job: 1, stage: 0 })
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadEmployees() {
  loading.value = true
  try {
    // 使用分页查询接口，获取第一页所有数据
    const res = await listEmployees({ page: 1, pageSize: 100 })
    employees.value = res.rows || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingEmp.value = null
  form.value = { name: '', username: '', password: '', job: 1, stage: 0 }
  showModal.value = true
}

function openEdit(emp) {
  editingEmp.value = emp
  form.value = { name: emp.name, username: emp.username, password: '', job: emp.job, stage: emp.stage }
  showModal.value = true
}

async function handleSubmit() {
  if (!form.value.name || !form.value.username) {
    showToast('请填写完整信息')
    return
  }
  try {
    if (editingEmp.value) {
      await updateEmployee({ id: editingEmp.value.id, ...form.value })
      showToast('修改成功')
    } else {
      if (!form.value.password) {
        showToast('请输入密码')
        return
      }
      await createEmployee(form.value)
      showToast('创建成功')
    }
    showModal.value = false
    await loadEmployees()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

async function handleDelete(emp) {
  if (!confirm(`确定删除员工「${emp.name}」吗？`)) return
  try {
    await deleteEmployee(emp.id)
    showToast('删除成功')
    await loadEmployees()
  } catch (e) {
    showToast(e.message || '删除失败')
  }
}

onMounted(loadEmployees)
</script>

<template>
  <div class="employees">
    <div class="page-header">
      <h2>员工管理</h2>
      <button class="btn-primary" @click="openCreate">+ 新增员工</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="employees.length === 0" class="empty">
      <p>暂无员工数据</p>
    </div>

    <div v-else class="emp-list">
      <div v-for="emp in employees" :key="emp.id" class="emp-card">
        <div class="emp-info">
          <h3>{{ emp.name }}</h3>
          <p class="emp-meta">账号：{{ emp.username }} | 岗位：{{ emp.job === 0 ? '管理员' : '配送员' }} | 状态：{{ emp.stage === 0 ? '空闲' : '配送中' }}</p>
        </div>
        <div class="emp-actions">
          <button class="btn-outline" @click="openEdit(emp)">编辑</button>
          <button class="btn-danger" @click="handleDelete(emp)">删除</button>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ editingEmp ? '编辑员工' : '新增员工' }}</h3>
        <div class="form-group">
          <label>姓名</label>
          <input v-model="form.name" placeholder="输入姓名" />
        </div>
        <div class="form-group">
          <label>登录账号</label>
          <input v-model="form.username" placeholder="输入登录账号" />
        </div>
        <div class="form-group">
          <label>{{ editingEmp ? '新密码（留空不修改）' : '密码' }}</label>
          <input v-model="form.password" type="password" placeholder="输入密码" />
        </div>
        <div class="form-group">
          <label>岗位</label>
          <select v-model="form.job">
            <option :value="0">管理员</option>
            <option :value="1">配送员</option>
          </select>
        </div>
        <div class="form-group">
          <label>状态</label>
          <select v-model="form.stage">
            <option :value="0">空闲</option>
            <option :value="1">配送中</option>
          </select>
        </div>
        <div class="modal-actions">
          <button class="btn-outline" @click="showModal = false">取消</button>
          <button class="btn-primary" @click="handleSubmit">确认</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.employees { max-width: 900px; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  color: #e0e0e0;
  font-weight: 600;
}

.loading, .empty {
  text-align: center;
  color: #666;
  padding: 60px 0;
  font-size: 14px;
}

.emp-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.emp-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 18px 20px;
}

.emp-info h3 {
  font-size: 16px;
  color: #d4a853;
  margin-bottom: 4px;
}

.emp-meta {
  font-size: 13px;
  color: #666;
}

.emp-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
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

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  background: #141414;
  border: 1px solid #2a2a2a;
  color: #e0e0e0;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus {
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

.btn-primary:hover { background: #c49640; }

.btn-outline {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
}

.btn-outline:hover { color: #d4a853; border-color: #d4a853; }

.btn-danger {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
}

.btn-danger:hover {
  color: #c0392b;
  border-color: #c0392b;
  background: #1a0a0a;
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