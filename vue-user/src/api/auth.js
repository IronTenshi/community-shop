import api from './index'

/** 用户登录 */
export function userLogin({ username, password }) {
  return api.post('/user/login', { username, password })
}

/** 用户注册 */
export function userRegister({ username, password, phone, address }) {
  return api.post('/user/register', { username, password, phone, address })
}

/** 获取当前用户信息 */
export function getUserInfo() {
  return api.get('/user/me')
}

/** 修改用户信息 */
export function updateUser({ username, password, phone, address }) {
  return api.put('/user', { username, password, phone, address })
}