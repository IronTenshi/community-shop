<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addProduct, deleteProduct, updateProductStage, deleteProductImage, uploadImage, getProductsByShop } from '@/api/product-manage'
import { getMyShops } from '@/api/shop-manage'

const route = useRoute()
const router = useRouter()
const merId = Number(route.params.id)

const products = ref([])
const shop = ref(null)
const loading = ref(false)
const showModal = ref(false)
const form = reactive({
  name: '',
  describe: '',
  img: '',
  price: '',
  stage: 0,
})
const uploading = ref(false)
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => (toast.value = ''), 2000)
}

async function loadShopInfo() {
  try {
    const shops = await getMyShops()
    shop.value = (shops || []).find(s => s.id === merId)
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

async function loadProducts() {
  loading.value = true
  try {
    products.value = (await getProductsByShop(merId)) || []
  } catch (e) {
    showToast(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function handleFileChange(e) {
  const file = e.target.files[0]
  if (!file) return
  uploading.value = true
  try {
    const url = await uploadImage(file)
    form.img = url
    showToast('上传成功')
  } catch (e) {
    showToast(e.message || '上传失败')
  } finally {
    uploading.value = false
  }
}

function openAdd() {
  form.name = ''
  form.describe = ''
  form.img = ''
  form.price = ''
  form.stage = 0
  showModal.value = true
}

async function handleAdd() {
  if (!form.name || !form.describe || !form.img) {
    showToast('请填写完整信息并上传图片')
    return
  }
  try {
    await addProduct(merId, { ...form })
    showToast('添加成功')
    showModal.value = false
    await loadProducts()
  } catch (e) {
    showToast(e.message || '添加失败')
  }
}

async function handleDelete(product) {
  if (!confirm(`确定删除商品「${product.name}」吗？`)) return
  try {
    await deleteProduct(product.id)
    showToast('删除成功')
    await loadProducts()
  } catch (e) {
    showToast(e.message || '删除失败')
  }
}

async function toggleStage(product) {
  try {
    await updateProductStage(product.id, product.stage === 0 ? 1 : 0)
    product.stage = product.stage === 0 ? 1 : 0
    showToast(product.stage === 0 ? '已设置为有货' : '已设置为无货')
    await loadProducts()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

async function handleDeleteImage(product) {
  if (!confirm('确定删除该商品的图片吗？')) return
  try {
    await deleteProductImage(product.id)
    product.img = ''
    showToast('图片已删除')
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

onMounted(() => {
  loadShopInfo()
  loadProducts()
})
</script>

<template>
  <div class="shop-products">
    <div class="page-header">
      <div class="page-title">
        <button class="btn-back" @click="router.push('/merchant/shops')">← 返回</button>
        <h2>{{ shop?.name || '商品管理' }}</h2>
      </div>
      <button class="btn-primary" @click="openAdd">+ 添加商品</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else-if="products.length === 0" class="empty">
      <p>暂无商品，点击上方按钮添加</p>
    </div>

    <div v-else class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <div class="product-img">
          <img v-if="product.img" :src="product.img" :alt="product.name" />
          <div v-else class="no-image">暂无图片</div>
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="describe">{{ product.describe }}</p>
          <p v-if="product.price" class="price">¥{{ product.price.toFixed(2) }}</p>
          <span v-if="product.stage === 0" class="status in-stock">有货</span>
          <span v-else class="status out-stock">无货</span>
        </div>
        <div class="product-actions">
          <button
            class="btn-outline"
            :class="{ 'btn-success': product.stage === 1 }"
            @click="toggleStage(product)"
          >
            {{ product.stage === 0 ? '设为无货' : '设为有货' }}
          </button>
          <button v-if="product.img" class="btn-danger" @click="handleDeleteImage(product)">删图</button>
          <button class="btn-danger" @click="handleDelete(product)">删除</button>
        </div>
      </div>
    </div>

    <!-- 添加商品弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h3>添加商品</h3>
        <div class="form-group">
          <label>商品名称</label>
          <input v-model="form.name" placeholder="输入商品名称" />
        </div>
        <div class="form-group">
          <label>商品描述</label>
          <textarea v-model="form.describe" placeholder="输入商品描述" rows="3" />
        </div>
        <div class="form-group">
          <label>商品图片</label>
          <input type="file" accept="image/*" @change="handleFileChange" />
          <div v-if="form.img" class="preview">
            <img :src="form.img" alt="预览" />
          </div>
        </div>
        <div class="form-group">
          <label>商品价格</label>
          <input v-model="form.price" type="number" step="0.01" placeholder="输入商品价格" />
        </div>
        <div class="form-group">
          <label>商品状态</label>
          <select v-model="form.stage">
            <option :value="0">有货</option>
            <option :value="1">无货</option>
          </select>
        </div>
        <div class="modal-actions">
          <button class="btn-outline" @click="showModal = false">取消</button>
          <button class="btn-primary" @click="handleAdd" :disabled="uploading">
            {{ uploading ? '上传中...' : '添加' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast">{{ toast }}</div>
  </div>
</template>

<style scoped>
.shop-products { max-width: 1000px; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title h2 {
  font-size: 20px;
  color: #e0e0e0;
  font-weight: 600;
}

.btn-back {
  background: transparent;
  border: 1px solid #333;
  color: #888;
  padding: 6px 12px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-back:hover {
  color: #d4a853;
  border-color: #d4a853;
}

.loading, .empty {
  text-align: center;
  color: #666;
  padding: 60px 0;
  font-size: 14px;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.product-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  overflow: hidden;
}

.product-img {
  width: 100%;
  height: 180px;
  overflow: hidden;
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
  margin-bottom: 4px;
}

.describe {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  font-size: 16px;
  color: #d4a853;
  margin-bottom: 6px;
}

.status {
  display: inline-block;
  padding: 2px 8px;
  font-size: 11px;
  border-radius: 2px;
}

.in-stock {
  background: #0e2d18;
  color: #27ae60;
}

.out-stock {
  background: #2d0e0e;
  color: #e74c3c;
}

.product-actions {
  display: flex;
  border-top: 1px solid #2a2a2a;
}

.product-actions button {
  flex: 1;
  padding: 10px 0;
  border: none;
  border-left: 1px solid #2a2a2a;
  background: transparent;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.15s;
}

.product-actions button:first-child {
  border-left: none;
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
  width: 480px;
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

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  background: #141414;
  border: 1px solid #2a2a2a;
  color: #e0e0e0;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  border-color: #d4a853;
}

.form-group textarea {
  resize: vertical;
}

.preview {
  margin-top: 8px;
  width: 100px;
  height: 100px;
  border: 1px solid #2a2a2a;
  overflow: hidden;
}

.preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  transition: all 0.15s;
}

.btn-outline:hover:not(:disabled) {
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

.btn-danger:hover:not(:disabled) {
  color: #c0392b;
  border-color: #c0392b;
  background: #1a0a0a;
}

.btn-success:hover:not(:disabled) {
  color: #27ae60;
  border-color: #27ae60;
  background: #0e2d18;
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
