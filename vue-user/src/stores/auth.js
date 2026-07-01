import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getUserInfo } from '@/api/auth'

function getValidToken() {
  const t = localStorage.getItem('token')
  if (!t || t === 'null' || t === 'undefined') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    return ''
  }
  return t
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getValidToken())
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.username || '')

  /** 设置登录态 */
  function setAuth(data) {
    if (!data || !data.token) {
      console.error('登录数据无效:', data)
      throw new Error('登录失败：未获取到有效token')
    }
    token.value = data.token
    userInfo.value = { id: data.id, username: data.username }
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  /** 刷新用户信息 */
  async function refreshUserInfo() {
    if (!token.value) return
    const data = await getUserInfo()
    // 更新本地存储
    userInfo.value = {
      ...userInfo.value,
      id: data.id,
      username: data.username,
      phone: data.phone,
      address: data.address,
    }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  /** 退出登录 */
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userInfo, isLoggedIn, username, setAuth, refreshUserInfo, logout }
})