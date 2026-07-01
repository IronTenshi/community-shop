<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { createOrder } from '@/api/order'

const router = useRouter()
const cart = useCartStore()

const reAddress = ref('')
const submitting = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function submitOrder() {
  if (cart.items.length === 0) {
    showToast('购物车为空')
    return
  }
  if (!reAddress.value.trim()) {
    showToast('请填写收货地址')
    return
  }
  submitting.value = true
  try {
    const orderItems = cart.items.map((i) => ({ proId: i.product.id, num: i.num }))
    await createOrder({ reAddress: reAddress.value, orderItems })
    showToast('下单成功')
    cart.clearCart()
    router.push('/orders')
  } catch (e) {
    showToast(e.message || '下单失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="cart-page">
    <h2 class="page-title">确认订单</h2>

    <div class="section">
      <label class="section-label">收货地址</label>
      <input v-model="reAddress" placeholder="请输入详细收货地址" class="addr-input" />
    </div>

    <div class="section">
      <label class="section-label">商品明细</label>
      <div v-if="cart.items.length === 0" class="empty-cart">
        <p>购物车为空</p>
        <router-link to="/products" class="go-link">去选购商品</router-link>
      </div>

      <div v-else class="cart-table">
        <div class="table-header">
          <span class="col-name">商品</span>
          <span class="col-price">单价</span>
          <span class="col-num">数量</span>
          <span class="col-total">小计</span>
          <span class="col-act">操作</span>
        </div>
        <div v-for="item in cart.items" :key="item.product.id" class="table-row">
          <span class="col-name">{{ item.product.name }}</span>
          <span class="col-price">&yen;{{ item.product.price.toFixed(2) }}</span>
          <span class="col-num">
            <button @click="cart.updateNum(item.product.id, item.num - 1)">-</button>
            <span>{{ item.num }}</span>
            <button @click="cart.updateNum(item.product.id, item.num + 1)">+</button>
          </span>
          <span class="col-total">&yen;{{ (item.product.price * item.num).toFixed(2) }}</span>
          <span class="col-act">
            <button class="del-btn" @click="cart.removeFromCart(item.product.id)">删除</button>
          </span>
        </div>
      </div>
    </div>

    <div v-if="cart.items.length > 0" class="summary-bar">
      <div class="summary-info">
        <span>共 {{ cart.totalCount }} 件商品</span>
        <span class="summary-price">&yen;{{ cart.totalPrice.toFixed(2) }}</span>
      </div>
      <button class="submit-btn" :disabled="submitting" @click="submitOrder">
        {{ submitting ? '提交中...' : '提交订单' }}
      </button>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.cart-page {
  position: relative;
}

.page-title {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 500;
  letter-spacing: 1px;
  margin-bottom: 24px;
}

.section {
  margin-bottom: 24px;
}

.section-label {
  display: block;
  margin-bottom: 8px;
  font-size: 12px;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.addr-input {
  width: 100%;
  padding: 10px 12px;
  background: #141414;
  border: 1px solid #333;
  color: #e0e0e0;
  font-size: 14px;
  outline: none;
}

.addr-input:focus {
  border-color: #d4a853;
}

.addr-input::placeholder {
  color: #555;
}

.empty-cart {
  text-align: center;
  padding: 40px 20px;
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
}

.empty-cart p {
  color: #666;
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

.cart-table {
  border: 1px solid #2a2a2a;
}

.table-header {
  display: grid;
  grid-template-columns: 2fr 1fr 120px 100px 80px;
  padding: 10px 16px;
  background: #141414;
  font-size: 12px;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-bottom: 1px solid #2a2a2a;
}

.table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 120px 100px 80px;
  padding: 12px 16px;
  align-items: center;
  font-size: 14px;
  color: #ccc;
  border-bottom: 1px solid #222;
}

.col-num {
  display: flex;
  align-items: center;
  gap: 8px;
}

.col-num button {
  width: 24px;
  height: 24px;
  background: #141414;
  border: 1px solid #333;
  color: #888;
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
}

.col-num button:hover {
  border-color: #d4a853;
  color: #d4a853;
}

.col-total {
  color: #d4a853;
  font-weight: 500;
}

.del-btn {
  padding: 4px 10px;
  background: transparent;
  border: 1px solid #333;
  color: #888;
  cursor: pointer;
  font-size: 12px;
}

.del-btn:hover {
  border-color: #c0392b;
  color: #c0392b;
}

.summary-bar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
  padding: 16px 0;
  border-top: 1px solid #2a2a2a;
}

.summary-info {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #888;
}

.summary-price {
  color: #d4a853;
  font-size: 18px;
  font-weight: 600;
}

.submit-btn {
  padding: 10px 32px;
  background: #d4a853;
  color: #1a1a1a;
  border: none;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 1px;
  cursor: pointer;
  text-transform: uppercase;
}

.submit-btn:disabled {
  opacity: 0.5;
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