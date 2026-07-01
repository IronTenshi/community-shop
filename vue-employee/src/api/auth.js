import api from './index'

/** 员工登录 */
export function empLogin({ username, password }) {
  return api.post('/emp/login', { username, password })
}