<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { getShopProducts } from '@/api/product'
import { createOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const cart = useCartStore()

const shop = ref(null)
const products = ref([])
const loading = ref(false)
const toast = ref('')
const submitting = ref(false)

// 立即购买弹窗
const showBuyModal = ref(false)
const buyProduct = ref(null)
const buyNum = ref(1)
const buyAddress = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadProducts() {
  const merId = route.params.id
  loading.value = true
  try {
    const data = await getShopProducts(merId)
    products.value = data
    // 从商品数据中提取商铺信息
    if (data.length > 0) {
      shop.value = { id: merId }
    }
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function handleAddToCart(product) {
  cart.addToCart(product, 1)
  showToast(`已添加「${product.name}」到购物车`)
}

function handleBuyNow(product) {
  buyProduct.value = product
  buyNum.value = 1
  buyAddress.value = ''
  showBuyModal.value = true
}

async function submitBuy() {
  if (!buyAddress.value.trim()) {
    showToast('请填写收货地址')
    return
  }
  submitting.value = true
  try {
    await createOrder({
      reAddress: buyAddress.value,
      orderItems: [{ proId: buyProduct.value.id, num: buyNum.value }],
    })
    showToast('下单成功')
    showBuyModal.value = false
    router.push('/orders')
  } catch (e) {
    showToast(e.message || '下单失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<template>
  <div class="shop-detail">
    <!-- 头部 -->
    <div class="shop-header">
      <button class="back-btn" @click="router.push('/shops')">← 返回商铺列表</button>
      <h2 class="shop-title">商铺商品</h2>
      <p class="shop-id">商铺 ID: {{ route.params.id }}</p>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 商品列表 -->
    <div v-else-if="products.length > 0" class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <div class="product-img">
          <img v-if="product.img" :src="product.img" :alt="product.name" />
          <div v-else class="no-image">暂无图片</div>
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="describe">{{ product.describe }}</p>
          <p class="price">¥{{ product.price.toFixed(2) }}</p>
          <span v-if="product.stage === 0" class="status in-stock">有货</span>
          <span v-else class="status out-stock">无货</span>
        </div>
        <div class="product-actions">
          <button
            class="btn-cart"
            :disabled="product.stage !== 0"
            @click="handleAddToCart(product)"
          >
            加入购物车
          </button>
          <button
            class="btn-buy"
            :disabled="product.stage !== 0"
            @click="handleBuyNow(product)"
          >
            立即购买
          </button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty">
      <p>该商铺暂无商品</p>
      <button class="back-btn" @click="router.push('/shops')">返回商铺列表</button>
    </div>

    <!-- 立即购买弹窗 -->
    <div v-if="showBuyModal" class="modal-overlay" @click.self="showBuyModal = false">
      <div class="modal">
        <h3>立即购买</h3>
        <div class="buy-product-name">{{ buyProduct?.name }}</div>
        <div class="buy-product-price">¥{{ buyProduct?.price?.toFixed(2) }}</div>

        <div class="form-group">
          <label>购买数量</label>
          <div class="num-control">
            <button @click="buyNum = Math.max(1, buyNum - 1)">-</button>
            <span>{{ buyNum }}</span>
            <button @click="buyNum = buyNum + 1">+</button>
          </div>
        </div>

        <div class="form-group">
          <label>收货地址</label>
          <input v-model="buyAddress" placeholder="请输入收货地址" />
        </div>

        <div class="buy-total">
          合计：<span class="total-price">¥{{ ((buyProduct?.price || 0) * buyNum).toFixed(2) }}</span>
        </div>

        <div class="modal-actions">
          <button class="btn-outline" @click="showBuyModal = false">取消</button>
          <button class="btn-primary" :disabled="submitting" @click="submitBuy">
            {{ submitting ? '提交中...' : '确认下单' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.shop-detail {
  position: relative;
}

/* 头部 */
.shop-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #2a2a2a;
}

.back-btn {
  background: none;
  border: none;
  color: #888;
  font-size: 13px;
  cursor: pointer;
  padding: 0;
  margin-bottom: 12px;
  transition: color 0.15s;
}

.back-btn:hover {
  color: #d4a853;
}

.shop-title {
  font-size: 20px;
  color: #e0e0e0;
  font-weight: 600;
  letter-spacing: 1px;
}

.shop-id {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

/* 加载与空 */
.loading {
  text-align: center;
  padding: 60px 20px;
  color: #888;
  font-size: 14px;
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: #666;
  font-size: 14px;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.empty p {
  margin-bottom: 16px;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
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
  height: 180px;
  overflow: hidden;
  background: #141414;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-img .no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #141414;
  color: #666;
  font-size: 14px;
}

.product-info {
  padding: 14px 16px;
}

.product-info h3 {
  font-size: 15px;
  color: #e0e0e0;
  margin-bottom: 6px;
  font-weight: 500;
}

.describe {
  font-size: 12px;
  color: #777;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  font-size: 16px;
  color: #d4a853;
  font-weight: 600;
  margin-bottom: 6px;
}

.status {
  font-size: 11px;
  padding: 2px 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.in-stock {
  color: #27ae60;
  border: 1px solid #27ae60;
}

.out-stock {
  color: #888;
  border: 1px solid #444;
}

.product-actions {
  display: flex;
  gap: 0;
  border-top: 1px solid #2a2a2a;
}

.btn-cart,
.btn-buy {
  flex: 1;
  padding: 10px 0;
  font-size: 12px;
  cursor: pointer;
  border: none;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.15s;
}

.btn-cart {
  background: #252525;
  color: #ccc;
  border-right: 1px solid #2a2a2a;
}

.btn-cart:hover:not(:disabled) {
  background: #2a2a2a;
  color: #e0e0e0;
}

.btn-buy {
  background: #d4a853;
  color: #141414;
  font-weight: 600;
}

.btn-buy:hover:not(:disabled) {
  background: #c49640;
}

.btn-cart:disabled,
.btn-buy:disabled {
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
  width: 400px;
}

.modal h3 {
  font-size: 18px;
  color: #e0e0e0;
  margin-bottom: 16px;
}

.buy-product-name {
  font-size: 15px;
  color: #e0e0e0;
  margin-bottom: 4px;
}

.buy-product-price {
  font-size: 20px;
  color: #d4a853;
  font-weight: 600;
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

.num-control {
  display: flex;
  align-items: center;
  gap: 12px;
}

.num-control button {
  width: 32px;
  height: 32px;
  background: #141414;
  border: 1px solid #333;
  color: #888;
  cursor: pointer;
  font-size: 16px;
  line-height: 1;
}

.num-control button:hover {
  border-color: #d4a853;
  color: #d4a853;
}

.num-control span {
  font-size: 16px;
  color: #e0e0e0;
  min-width: 20px;
  text-align: center;
}

.buy-total {
  font-size: 14px;
  color: #888;
  margin-bottom: 20px;
  padding-top: 16px;
  border-top: 1px solid #2a2a2a;
}

.total-price {
  color: #d4a853;
  font-size: 18px;
  font-weight: 600;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
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

.btn-primary:hover:not(:disabled) {
  background: #c49640;
  border-color: #c49640;
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  padding: 10px 24px;
  background: #1e1e1e;
  border: 1px solid #d4a853;
  color: #d4a853;
  font-size: 13px;
  z-index: 200;
}
</style>