<script setup>
import { ref, onMounted } from 'vue'
import { useCartStore } from '@/stores/cart'
import { getProducts } from '@/api/product'

const cart = useCartStore()

const products = ref([])
const loading = ref(false)
const searchName = ref('')
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadProducts() {
  loading.value = true
  try {
    const res = await getProducts({ name: searchName.value })
    products.value = res.rows || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function addToCart(product) {
  if (product.stage !== 0) {
    showToast('该商品已下架')
    return
  }
  cart.addToCart(product)
  showToast('已加入购物车')
}

onMounted(loadProducts)
</script>

<template>
  <div class="products-page">
    <div class="page-header">
      <h2>商品选购</h2>
      <div class="search-bar">
        <input
          v-model="searchName"
          @keyup.enter="loadProducts"
          placeholder="搜索商品名称..."
        />
        <button class="btn" @click="loadProducts" :disabled="loading">
          {{ loading ? '搜索中' : '搜索' }}
        </button>
      </div>
    </div>

    <div v-if="loading && products.length === 0" class="empty">加载中...</div>
    <div v-else-if="products.length === 0" class="empty">暂无商品</div>

    <div v-else class="products-grid">
      <div v-for="p in products" :key="p.id" class="product-card">
        <div class="product-img">
          <img v-if="p.img" :src="p.img" :alt="p.name" />
          <span v-else class="no-img">暂无图片</span>
        </div>
        <div class="product-body">
          <h3>{{ p.name }}</h3>
          <p class="desc">{{ p.describe }}</p>
          <div class="product-meta">
            <span class="price">&yen;{{ p.price.toFixed(2) }}</span>
            <span :class="['status', p.stage === 0 ? 'on' : 'off']">
              {{ p.stage === 0 ? '有货' : '无货' }}
            </span>
          </div>
          <button
            class="add-btn"
            :disabled="p.stage !== 0"
            @click="addToCart(p)"
          >
            加入购物车
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.products-page {
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

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

.product-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  overflow: hidden;
  transition: border-color 0.15s;
}

.product-card:hover {
  border-color: #444;
}

.product-img {
  width: 100%;
  height: 160px;
  background: #141414;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #2a2a2a;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-img {
  color: #555;
  font-size: 13px;
}

.product-body {
  padding: 16px;
}

.product-body h3 {
  font-size: 15px;
  color: #e0e0e0;
  margin-bottom: 6px;
}

.desc {
  font-size: 12px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.price {
  font-size: 18px;
  color: #d4a853;
  font-weight: 600;
}

.status {
  padding: 2px 8px;
  font-size: 11px;
  border: 1px solid;
}

.status.on {
  color: #27ae60;
  border-color: #27ae60;
}

.status.off {
  color: #888;
  border-color: #444;
}

.add-btn {
  width: 100%;
  padding: 8px 0;
  background: transparent;
  color: #d4a853;
  border: 1px solid #d4a853;
  font-size: 13px;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: all 0.15s;
}

.add-btn:hover:not(:disabled) {
  background: #d4a853;
  color: #1a1a1a;
}

.add-btn:disabled {
  color: #555;
  border-color: #333;
  cursor: not-allowed;
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