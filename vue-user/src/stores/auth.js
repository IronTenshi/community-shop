import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

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

  /** 退出登录 */
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userInfo, isLoggedIn, username, setAuth, logout }
})