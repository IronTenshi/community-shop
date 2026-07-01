import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8090',
  timeout: 10000,
})

// 请求拦截器：自动附加 token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    console.log('[axios] 请求 ' + config.method?.toUpperCase() + ' ' + config.url + ' | token=' + (token ? token.substring(0, 20) + '...' : 'null'))
    if (token && token !== 'null' && token !== 'undefined') {
      config.headers['token'] = token
    } else {
      console.warn('[axios] token无效，未附加到请求头')
    }
    return config
  },
  (err) => Promise.reject(err),
)

// 响应拦截器：统一解包 res.data.data（修复 token 获取不到的 bug）
api.interceptors.response.use(
  (res) => {
    if (res.data.code === 200) {
      return res.data.data // 关键修复：返回 data 字段，而非整个 {code, message, data}
    }
    return Promise.reject(new Error(res.data.message || '请求失败'))
  },
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  },
)

export default api