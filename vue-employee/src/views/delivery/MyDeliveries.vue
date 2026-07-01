<script setup>
import { ref, onMounted } from 'vue'
import { getMyDeliveries, completeOrder } from '@/api/delivery'

const deliveries = ref([])
const loading = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadDeliveries() {
  loading.value = true
  try {
    deliveries.value = (await getMyDeliveries()) || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function handleComplete(delivery) {
  if (!confirm('确认完成配送吗？')) return
  try {
    await completeOrder(delivery.ordId)
    showToast('配送完成')
    await loadDeliveries()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

const stageMap = { 0: '未派送', 1: '配送中', 2: '已送达' }

onMounted(loadDeliveries)
</script>

<template>
  <div class="my-deliveries">
    <div class="page-header">
      <h2>我的配送</h2>
      <button class="btn-outline" @click="loadDeliveries">刷新</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="deliveries.length === 0" class="empty">
      <p>暂无配送订单</p>
    </div>

    <div v-else class="delivery-list">
      <div v-for="d in deliveries" :key="d.delivery.id" class="delivery-card">
        <div class="delivery-header">
          <span class="delivery-id">配送 #{{ d.delivery.id }}</span>
          <span class="stage" :class="'stage-' + d.delivery.stage">{{ stageMap[d.delivery.stage] || d.delivery.stage }}</span>
        </div>
        <div class="delivery-body">
          <p class="address">收货地址：{{ d.order?.reAddress }}</p>
          <p class="money">金额：¥{{ d.order?.money?.toFixed(2) }}</p>
          <p v-if="d.delivery.note" class="note">备注：{{ d.delivery.note }}</p>
        </div>
        <div class="delivery-footer" v-if="d.delivery.stage === 1">
          <button class="btn-primary" @click="handleComplete(d.delivery)">完成配送</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.my-deliveries { max-width: 900px; }

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

.delivery-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.delivery-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.delivery-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #2a2a2a;
}

.delivery-id {
  font-size: 14px;
  color: #d4a853;
  font-weight: 600;
}

.stage {
  padding: 3px 10px;
  font-size: 11px;
  text-transform: uppercase;
}

.stage-0 { background: #2d1a0e; color: #e67e22; }
.stage-1 { background: #0e2d18; color: #27ae60; }
.stage-2 { background: #1a1a2e; color: #3498db; }

.delivery-body {
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
  margin-bottom: 4px;
}

.note {
  font-size: 12px;
  color: #666;
}

.delivery-footer {
  padding: 10px 16px;
  border-top: 1px solid #2a2a2a;
  text-align: right;
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