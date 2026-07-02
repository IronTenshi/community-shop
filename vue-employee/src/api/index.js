import axios from 'axios'

const api = axios.create({
  baseURL: '/',
  timeout: 10000,
})

// 请求拦截器：自动附加 token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('empToken')
    if (token && token !== 'null' && token !== 'undefined') {
      config.headers['token'] = token
    }
    return config
  },
  (err) => Promise.reject(err),
)

// 响应拦截器：解包 data 并处理 401
api.interceptors.response.use(
  (res) => {
    if (res.data.code === 200) {
      return res.data.data
    }
    return Promise.reject(new Error(res.data.message || '请求失败'))
  },
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('empToken')
      localStorage.removeItem('empInfo')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  },
)

export default api