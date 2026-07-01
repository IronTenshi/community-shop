<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getShops } from '@/api/shop'

const router = useRouter()
const shops = ref([])
const searchName = ref('')
const loading = ref(false)
const toast = ref('')
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadShops() {
  loading.value = true
  try {
    const res = await getShops({ page: page.value, pageSize: pageSize.value, name: searchName.value || undefined })
    shops.value = res.rows || []
    total.value = res.total || 0
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function goPage(p) {
  if (p < 1 || p > totalPages.value) return
  page.value = p
  loadShops()
}

function search() {
  page.value = 1
  loadShops()
}

function goShop(shop) {
  router.push(`/shops/${shop.id}`)
}

onMounted(() => {
  loadShops()
})
</script>

<template>
  <div class="shops-page">
    <div class="page-header">
      <h2>商铺浏览</h2>
      <div class="search-bar">
        <input
          v-model="searchName"
          @keyup.enter="search"
          placeholder="搜索商铺名称..."
        />
        <button class="btn" @click="search" :disabled="loading">
          {{ loading ? '搜索中' : '搜索' }}
        </button>
      </div>
    </div>

    <div v-if="loading && shops.length === 0" class="empty">加载中...</div>
    <div v-else-if="shops.length === 0" class="empty">暂无商铺</div>

    <div v-else class="shops-grid">
      <div v-for="s in shops" :key="s.id" class="shop-card" @click="goShop(s)">
        <div class="shop-body">
          <h3>{{ s.name }} <span class="arrow">→</span></h3>
          <div class="shop-meta">
            <span>联系人：{{ s.person || '-' }}</span>
            <span>电话：{{ s.phone || '-' }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="total > 0" class="pagination">
      <span class="page-info">共 {{ total }} 条</span>
      <button class="page-btn" :disabled="page <= 1" @click="goPage(page - 1)">上一页</button>
      <span class="page-num">{{ page }} / {{ totalPages }}</span>
      <button class="page-btn" :disabled="page >= totalPages" @click="goPage(page + 1)">下一页</button>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.shops-page {
  position: relative;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.page-header h2 {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 500;
  letter-spacing: 1px;
}

.search-bar {
  display: flex;
  gap: 0;
}

.search-bar input {
  width: 220px;
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

.btn {
  padding: 8px 16px;
  background: #d4a853;
  color: #1a1a1a;
  border: 1px solid #d4a853;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 1px;
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

.shops-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.shop-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  overflow: hidden;
  transition: border-color 0.15s;
  cursor: pointer;
}

.shop-card:hover {
  border-color: #d4a853;
}

.arrow {
  color: #666;
  font-size: 14px;
  transition: color 0.15s;
}

.shop-card:hover .arrow {
  color: #d4a853;
}

.shop-body {
  padding: 16px;
}

.shop-body h3 {
  font-size: 16px;
  color: #e0e0e0;
  margin-bottom: 10px;
}

.shop-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #888;
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 24px;
  padding: 16px 0;
}

.page-info {
  font-size: 12px;
  color: #666;
}

.page-num {
  font-size: 13px;
  color: #aaa;
  min-width: 60px;
  text-align: center;
}

.page-btn {
  padding: 6px 14px;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 12px;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.15s;
}

.page-btn:hover:not(:disabled) {
  border-color: #d4a853;
  color: #d4a853;
}

.page-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
</style>