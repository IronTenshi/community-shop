<script setup>
import { ref, onMounted } from 'vue'
import { getMyOrders, cancelOrder } from '@/api/order'

const orders = ref([])
const loading = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadOrders() {
  loading.value = true
  try {
    const data = await getMyOrders()
    orders.value = data || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function doCancel(id) {
  if (!confirm('确认取消该订单？')) return
  try {
    await cancelOrder(id)
    showToast('订单已取消')
    loadOrders()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

function statusText(stage) {
  return stage === 0 ? '配送中' : '已送达'
}

function statusClass(stage) {
  return stage === 0 ? 'pending' : 'done'
}

onMounted(loadOrders)
</script>

<template>
  <div class="orders-page">
    <div class="page-header">
      <h2>我的订单</h2>
      <button class="btn" @click="loadOrders" :disabled="loading">刷新</button>
    </div>

    <div v-if="loading && orders.length === 0" class="empty">加载中...</div>
    <div v-else-if="orders.length === 0" class="empty">
      <p>暂无订单</p>
      <router-link to="/products" class="go-link">去选购商品</router-link>
    </div>

    <div v-else class="order-list">
      <div v-for="o in orders" :key="o.id" class="order-card">
        <div class="order-head">
          <span>订单号：{{ o.id }}</span>
          <span :class="['order-status', statusClass(o.stage)]">
            {{ statusText(o.stage) }}
          </span>
        </div>
        <div class="order-body">
          <div class="row">
            <span class="label">金额</span>
            <span class="value price">&yen;{{ o.money.toFixed(2) }}</span>
          </div>
          <div class="row">
            <span class="label">收货地址</span>
            <span class="value">{{ o.reAddress }}</span>
          </div>
          <div class="row">
            <span class="label">创建时间</span>
            <span class="value">{{ o.createTime }}</span>
          </div>
        </div>
        <div v-if="o.stage === 0" class="order-foot">
          <button class="cancel-btn" @click="doCancel(o.id)">取消订单</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.orders-page {
  position: relative;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 500;
  letter-spacing: 1px;
}

.btn {
  padding: 7px 18px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 12px;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.btn:hover:not(:disabled) {
  border-color: #d4a853;
  color: #d4a853;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.empty {
  text-align: center;
  padding: 80px 20px;
  color: #555;
  font-size: 14px;
}

.empty p {
  margin-bottom: 12px;
}

.go-link {
  color: #d4a853;
  text-decoration: none;
  font-size: 13px;
}

.go-link:hover {
  text-decoration: underline;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  border-left: 3px solid #333;
}

.order-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
  border-bottom: 1px solid #2a2a2a;
  font-size: 14px;
  color: #ccc;
}

.order-status {
  padding: 3px 10px;
  font-size: 11px;
  border: 1px solid;
  text-transform: uppercase;
}

.order-status.pending {
  color: #d4a853;
  border-color: #d4a853;
}

.order-status.done {
  color: #27ae60;
  border-color: #27ae60;
}

.order-body {
  padding: 16px 20px;
}

.row {
  display: flex;
  margin-bottom: 8px;
  font-size: 13px;
}

.row:last-child {
  margin-bottom: 0;
}

.label {
  color: #666;
  width: 80px;
  flex-shrink: 0;
}

.value {
  color: #aaa;
}

.price {
  color: #d4a853;
  font-weight: 500;
}

.order-foot {
  padding: 12px 20px;
  border-top: 1px solid #2a2a2a;
  text-align: right;
}

.cancel-btn {
  padding: 6px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 12px;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.cancel-btn:hover {
  border-color: #c0392b;
  color: #c0392b;
}

.toast {
  position: fixed;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  padding: 10px 24px;
  background: #1e1e1e;
  border: 1px solid #d4a853;
  color: #d4a853;
  font-size: 13px;
  z-index: 999;
}
</style>