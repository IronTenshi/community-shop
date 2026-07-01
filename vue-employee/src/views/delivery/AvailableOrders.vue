<script setup>
import { ref, onMounted } from 'vue'
import { getAvailableOrders, acceptOrder } from '@/api/delivery'

const orders = ref([])
const loading = ref(false)
const toast = ref('')
const note = ref('')
const acceptingId = ref(null)

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadOrders() {
  loading.value = true
  try {
    orders.value = (await getAvailableOrders()) || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openAccept(ordId) {
  acceptingId.value = ordId
  note.value = ''
}

async function handleAccept() {
  if (!acceptingId.value) return
  try {
    await acceptOrder(acceptingId.value, note.value)
    showToast('接单成功')
    acceptingId.value = null
    await loadOrders()
  } catch (e) {
    showToast(e.message || '接单失败')
  }
}

onMounted(loadOrders)
</script>

<template>
  <div class="available-orders">
    <div class="page-header">
      <h2>空闲订单</h2>
      <button class="btn-outline" @click="loadOrders">刷新</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="orders.length === 0" class="empty">
      <p>暂无空闲订单</p>
    </div>

    <div v-else class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-id">订单 #{{ order.id }}</span>
          <span class="order-time">{{ order.createTime }}</span>
        </div>
        <div class="order-body">
          <div class="order-info">
            <p class="address">收货地址：{{ order.reAddress }}</p>
            <p class="money">金额：¥{{ order.money?.toFixed(2) }}</p>
          </div>
          <div class="order-items" v-if="order.orderItems?.length">
            <span v-for="item in order.orderItems" :key="item.id" class="item-tag">
              {{ item.productName }} x{{ item.num }}
            </span>
          </div>
        </div>
        <div class="order-footer">
          <button class="btn-primary" @click="openAccept(order.id)">接单</button>
        </div>
      </div>
    </div>

    <!-- 接单备注弹窗 -->
    <div v-if="acceptingId" class="modal-overlay" @click.self="acceptingId = null">
      <div class="modal">
        <h3>确认接单</h3>
        <div class="form-group">
          <label>备注（可选）</label>
          <input v-model="note" placeholder="输入备注信息" />
        </div>
        <div class="modal-actions">
          <button class="btn-outline" @click="acceptingId = null">取消</button>
          <button class="btn-primary" @click="handleAccept">确认接单</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.available-orders { max-width: 900px; }

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

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #2a2a2a;
}

.order-id {
  font-size: 14px;
  color: #d4a853;
  font-weight: 600;
}

.order-time {
  font-size: 12px;
  color: #555;
}

.order-body {
  padding: 14px 16px;
}

.address {
  font-size: 14px;
  color: #ccc;
  margin-bottom: 4px;
}

.money {
  font-size: 16px;
  color: #d4a853;
  font-weight: 600;
  margin-bottom: 8px;
}

.order-items {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.item-tag {
  padding: 3px 8px;
  background: #141414;
  border: 1px solid #2a2a2a;
  font-size: 12px;
  color: #888;
}

.order-footer {
  padding: 10px 16px;
  border-top: 1px solid #2a2a2a;
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
  width: 400px;
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

.btn-primary:hover {
  background: #c49640;
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