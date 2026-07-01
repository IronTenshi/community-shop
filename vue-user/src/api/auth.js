import api from './index'

/** 用户登录 */
export function userLogin({ username, password }) {
  return api.post('/user/login', { username, password })
}

/** 用户注册 */
export function userRegister({ username, password, phone, address }) {
  return api.post('/user/register', { username, password, phone, address })
}