<script setup>
import { ref, onMounted } from 'vue'
import { getAllOrders, getOrderDetail } from '@/api/admin'

const orders = ref([])
const orderDetail = ref(null)
const loading = ref(false)
const showDetail = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadOrders() {
  loading.value = true
  try {
    orders.value = (await getAllOrders()) || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function viewDetail(id) {
  try {
    orderDetail.value = await getOrderDetail(id)
    showDetail.value = true
  } catch (e) {
    showToast(e.message || '获取失败')
  }
}

const stageMap = { 0: '未派送', 1: '配送中', 2: '已送达' }

onMounted(loadOrders)
</script>

<template>
  <div class="all-orders">
    <div class="page-header">
      <h2>全部订单</h2>
      <button class="btn-outline" @click="loadOrders">刷新</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="orders.length === 0" class="empty">
      <p>暂无订单</p>
    </div>

    <div v-else class="order-table-wrap">
      <table class="order-table">
        <thead>
          <tr>
            <th>订单ID</th>
            <th>收货地址</th>
            <th>金额</th>
            <th>用户ID</th>
            <th>创建时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="o in orders" :key="o.id">
            <td class="id-cell">#{{ o.id }}</td>
            <td>{{ o.reAddress }}</td>
            <td class="money-cell">¥{{ o.money?.toFixed(2) }}</td>
            <td>{{ o.usId }}</td>
            <td class="time-cell">{{ o.createTime }}</td>
            <td>
              <span class="stage" :class="'stage-' + o.stage">
                {{ o.stage === 0 ? '进行中' : '已完成' }}
              </span>
            </td>
            <td>
              <button class="btn-link" @click="viewDetail(o.id)">详情</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 订单详情弹窗 -->
    <div v-if="showDetail && orderDetail" class="modal-overlay" @click.self="showDetail = false">
      <div class="modal">
        <h3>订单详情 #{{ orderDetail.order?.id }}</h3>
        <div class="detail-section">
          <p><strong>收货地址：</strong>{{ orderDetail.order?.reAddress }}</p>
          <p><strong>金额：</strong>¥{{ orderDetail.order?.money?.toFixed(2) }}</p>
          <p><strong>状态：</strong>{{ orderDetail.order?.stage === 0 ? '进行中' : '已完成' }}</p>
          <p><strong>创建时间：</strong>{{ orderDetail.order?.createTime }}</p>
        </div>
        <div v-if="orderDetail.delivery" class="delivery-section">
          <h4>配送信息</h4>
          <p><strong>配送员ID：</strong>{{ orderDetail.delivery.empId }}</p>
          <p><strong>配送状态：</strong>{{ stageMap[orderDetail.delivery.stage] || orderDetail.delivery.stage }}</p>
          <p v-if="orderDetail.delivery.note"><strong>备注：</strong>{{ orderDetail.delivery.note }}</p>
        </div>
        <div v-else class="delivery-section">
          <p class="no-delivery">暂无配送信息</p>
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
.all-orders { max-width: 1100px; }

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

.order-table-wrap {
  overflow-x: auto;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.order-table th {
  padding: 12px 14px;
  text-align: left;
  font-size: 12px;
  color: #777;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 1px solid #2a2a2a;
  background: #141414;
}

.order-table td {
  padding: 12px 14px;
  font-size: 13px;
  color: #ccc;
  border-bottom: 1px solid #1a1a1a;
}

.order-table tr:hover td {
  background: #1a1a1a;
}

.id-cell { color: #d4a853; font-weight: 600; }
.money-cell { color: #d4a853; font-weight: 600; }
.time-cell { font-size: 12px; color: #666; }

.stage {
  padding: 2px 8px;
  font-size: 11px;
}

.stage-0 { background: #2d1a0e; color: #e67e22; }
.stage-1 { background: #0e2d18; color: #27ae60; }

.btn-link {
  background: transparent;
  border: none;
  color: #d4a853;
  font-size: 12px;
  cursor: pointer;
  text-decoration: underline;
}

.btn-link:hover { color: #c49640; }

.btn-outline {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
}

.btn-outline:hover { color: #d4a853; border-color: #d4a853; }

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
  width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal h3 {
  font-size: 18px;
  color: #e0e0e0;
  margin-bottom: 20px;
}

.modal h4 {
  font-size: 14px;
  color: #d4a853;
  margin-bottom: 8px;
}

.detail-section, .delivery-section {
  margin-bottom: 16px;
  padding: 12px;
  background: #141414;
  border: 1px solid #2a2a2a;
}

.detail-section p, .delivery-section p {
  font-size: 13px;
  color: #ccc;
  margin-bottom: 4px;
  line-height: 1.6;
}

.detail-section strong, .delivery-section strong {
  color: #888;
}

.no-delivery {
  color: #555;
  font-style: italic;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
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