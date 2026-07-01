import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const totalCount = computed(() => items.value.reduce((s, i) => s + i.num, 0))
  const totalPrice = computed(() => items.value.reduce((s, i) => s + i.product.price * i.num, 0))

  function addToCart(product, num = 1) {
    const exist = items.value.find((i) => i.product.id === product.id)
    if (exist) {
      exist.num += num
    } else {
      items.value.push({ product, num })
    }
  }

  function removeFromCart(productId) {
    items.value = items.value.filter((i) => i.product.id !== productId)
  }

  function updateNum(productId, num) {
    const item = items.value.find((i) => i.product.id === productId)
    if (item) item.num = Math.max(1, num)
  }

  function clearCart() {
    items.value = []
  }

  return { items, totalCount, totalPrice, addToCart, removeFromCart, updateNum, clearCart }
})