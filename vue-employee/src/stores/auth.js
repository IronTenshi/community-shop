import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

function getValidToken() {
  const t = localStorage.getItem('empToken')
  if (!t || t === 'null' || t === 'undefined') {
    localStorage.removeItem('empToken')
    localStorage.removeItem('empInfo')
    return ''
  }
  return t
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getValidToken())
  const userInfo = ref(JSON.parse(localStorage.getItem('empInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.job === 0)
  const isDelivery = computed(() => userInfo.value?.job === 1)

  /** 设置登录态 */
  function setAuth(data) {
    if (!data || !data.token) {
      throw new Error('登录失败：未获取到有效token')
    }
    token.value = data.token
    userInfo.value = { id: data.id, username: data.username, job: data.job }
    localStorage.setItem('empToken', data.token)
    localStorage.setItem('empInfo', JSON.stringify(userInfo.value))
  }

  /** 退出登录 */
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('empToken')
    localStorage.removeItem('empInfo')
  }

  return { token, userInfo, isLoggedIn, isAdmin, isDelivery, setAuth, logout }
})