<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getShops } from '@/api/shop'

const router = useRouter()
const shops = ref([])
const searchName = ref('')
const loading = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadShops() {
  loading.value = true
  try {
    const res = await getShops({ name: searchName.value || undefined })
    shops.value = res.rows || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
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
          @keyup.enter="loadShops"
          placeholder="搜索商铺名称..."
        />
        <button class="btn" @click="loadShops" :disabled="loading">
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
</style>