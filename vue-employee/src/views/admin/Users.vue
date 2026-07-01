<script setup>
import { ref, onMounted, computed } from 'vue'
import { listUsers, getUserDetail } from '@/api/admin'

const users = ref([])
const total = ref(0)
const loading = ref(false)
const searchName = ref('')
const page = ref(1)
const pageSize = ref(10)
const toast = ref('')

// 用户详情弹窗
const showDetail = ref(false)
const detailUser = ref(null)

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadUsers() {
  loading.value = true
  try {
    const res = await listUsers({
      page: page.value,
      pageSize: pageSize.value,
      username: searchName.value || undefined,
    })
    users.value = res.rows || []
    total.value = res.total || 0
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  loadUsers()
}

function goPage(p) {
  page.value = p
  loadUsers()
}

async function viewDetail(user) {
  try {
    const data = await getUserDetail(user.id)
    detailUser.value = data
    showDetail.value = true
  } catch (e) {
    showToast(e.message || '获取用户详情失败')
  }
}

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))

onMounted(loadUsers)
</script>

<template>
  <div class="users">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input
        v-model="searchName"
        @keyup.enter="handleSearch"
        placeholder="搜索用户名..."
      />
      <button class="btn-primary" @click="handleSearch" :disabled="loading">
        {{ loading ? '搜索中' : '搜索' }}
      </button>
    </div>

    <!-- 加载中 -->
    <div v-if="loading && users.length === 0" class="loading">加载中...</div>

    <!-- 空状态 -->
    <div v-else-if="users.length === 0" class="empty">
      <p>暂无用户数据</p>
    </div>

    <!-- 用户表格 -->
    <div v-else class="table-wrap">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>手机号</th>
            <th>收货地址</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.phone || '-' }}</td>
            <td class="addr-cell">{{ user.address || '-' }}</td>
            <td>
              <button class="btn-outline" @click="viewDetail(user)">详情</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 分页 -->
      <div class="pagination">
        <span class="page-info">共 {{ total }} 条，第 {{ page }} / {{ totalPages }} 页</span>
        <div class="page-btns">
          <button :disabled="page <= 1" @click="goPage(page - 1)">上一页</button>
          <button :disabled="page >= totalPages" @click="goPage(page + 1)">下一页</button>
        </div>
      </div>
    </div>

    <!-- 用户详情弹窗 -->
    <div v-if="showDetail" class="modal-overlay" @click.self="showDetail = false">
      <div class="modal">
        <h3>用户详情</h3>
        <div class="detail-row">
          <span class="label">用户ID</span>
          <span class="value">{{ detailUser?.id }}</span>
        </div>
        <div class="detail-row">
          <span class="label">用户名</span>
          <span class="value">{{ detailUser?.username }}</span>
        </div>
        <div class="detail-row">
          <span class="label">手机号</span>
          <span class="value">{{ detailUser?.phone || '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">收货地址</span>
          <span class="value">{{ detailUser?.address || '-' }}</span>
        </div>
        <div class="modal-actions">
          <button class="btn-outline" @click="showDetail = false">关闭</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.users { max-width: 960px; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
  color: #e0e0e0;
  font-weight: 600;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  gap: 0;
  margin-bottom: 20px;
}

.search-bar input {
  width: 240px;
  padding: 8px 12px;
  background: #141414;
  border: 1px solid #333;
  border-right: none;
  color: #e0e0e0;
  font-size: 13px;
  outline: none;
}

.search-bar input:focus {
  border-color: #d4a853;
}

.search-bar input::placeholder {
  color: #555;
}

.loading, .empty {
  text-align: center;
  color: #666;
  padding: 60px 0;
  font-size: 14px;
}

/* 表格 */
.table-wrap {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 16px;
  text-align: left;
  font-size: 13px;
  border-bottom: 1px solid #222;
}

.data-table th {
  color: #888;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 12px;
  background: #141414;
}

.data-table td {
  color: #ccc;
}

.data-table tbody tr:hover {
  background: #1a1a1a;
}

.addr-cell {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #2a2a2a;
}

.page-info {
  font-size: 12px;
  color: #666;
}

.page-btns {
  display: flex;
  gap: 8px;
}

.page-btns button {
  padding: 6px 14px;
  background: #141414;
  border: 1px solid #333;
  color: #888;
  font-size: 12px;
  cursor: pointer;
}

.page-btns button:hover:not(:disabled) {
  border-color: #d4a853;
  color: #d4a853;
}

.page-btns button:disabled {
  opacity: 0.3;
  cursor: not-allowed;
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

.detail-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #222;
  font-size: 14px;
}

.detail-row:last-of-type {
  border-bottom: none;
}

.detail-row .label {
  color: #666;
  width: 90px;
  flex-shrink: 0;
}

.detail-row .value {
  color: #ccc;
  word-break: break-all;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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