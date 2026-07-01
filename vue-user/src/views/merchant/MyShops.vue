<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyShops, registerShop, updateShop, deleteShop } from '@/api/shop-manage'

const router = useRouter()
const shops = ref([])
const loading = ref(false)
const showModal = ref(false)
const editingShop = ref(null)
const form = ref({ name: '', phone: '', person: '' })
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadShops() {
  loading.value = true
  try {
    shops.value = (await getMyShops()) || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openRegister() {
  editingShop.value = null
  form.value = { name: '', phone: '', person: '' }
  showModal.value = true
}

function openEdit(shop) {
  editingShop.value = shop
  form.value = { name: shop.name, phone: shop.phone, person: shop.person }
  showModal.value = true
}

async function handleSubmit() {
  if (!form.value.name || !form.value.phone || !form.value.person) {
    showToast('请填写完整信息')
    return
  }
  try {
    if (editingShop.value) {
      await updateShop({ id: editingShop.value.id, ...form.value })
      showToast('修改成功')
    } else {
      await registerShop(form.value)
      showToast('注册成功')
    }
    showModal.value = false
    await loadShops()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

async function handleDelete(shop) {
  if (!confirm(`确定删除商铺「${shop.name}」吗？`)) return
  try {
    await deleteShop(shop.id)
    showToast('删除成功')
    await loadShops()
  } catch (e) {
    showToast(e.message || '删除失败')
  }
}

function goProducts(shop) {
  router.push(`/merchant/shops/${shop.id}`)
}

onMounted(loadShops)
</script>

<template>
  <div class="my-shops">
    <div class="page-header">
      <h2>我的商铺</h2>
      <button class="btn-primary" @click="openRegister">+ 注册商铺</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="shops.length === 0" class="empty">
      <p>暂无商铺，点击上方按钮注册</p>
    </div>

    <div v-else class="shop-list">
      <div v-for="shop in shops" :key="shop.id" class="shop-card">
        <div class="shop-info">
          <h3>{{ shop.name }}</h3>
          <p class="shop-meta">联系人：{{ shop.person }} | 电话：{{ shop.phone }}</p>
        </div>
        <div class="shop-actions">
          <button class="btn-outline" @click="goProducts(shop)">管理商品</button>
          <button class="btn-outline" @click="openEdit(shop)">编辑</button>
          <button class="btn-danger" @click="handleDelete(shop)">删除</button>
        </div>
      </div>
    </div>

    <!-- 注册/编辑弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ editingShop ? '编辑商铺' : '注册商铺' }}</h3>
        <div class="form-group">
          <label>商铺名称</label>
          <input v-model="form.name" placeholder="输入商铺名称" />
        </div>
        <div class="form-group">
          <label>联系人</label>
          <input v-model="form.person" placeholder="输入联系人" />
        </div>
        <div class="form-group">
          <label>联系电话</label>
          <input v-model="form.phone" placeholder="输入联系电话" />
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
.my-shops { max-width: 900px; }

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

/* 商铺卡片 */
.shop-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.shop-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 18px 20px;
}

.shop-card:hover {
  border-color: #3a3a3a;
}

.shop-info h3 {
  font-size: 16px;
  color: #d4a853;
  margin-bottom: 4px;
}

.shop-meta {
  font-size: 13px;
  color: #666;
}

.shop-actions {
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

/* 按钮 */
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
  transition: all 0.15s;
}

.btn-primary:hover {
  background: #c49640;
  border-color: #c49640;
}

.btn-outline {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-outline:hover {
  color: #d4a853;
  border-color: #d4a853;
}

.btn-danger {
  padding: 8px 16px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-danger:hover {
  color: #c0392b;
  border-color: #c0392b;
  background: #1a0a0a;
}

/* Toast */
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