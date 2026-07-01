import api from './index'

/** 获取空闲订单列表 */
export function getAvailableOrders() {
  return api.get('/delivery/emp/list')
}

/** 接单 */
export function acceptOrder(ordId, note) {
  return api.post(`/delivery/emp/${ordId}`, null, { params: { note } })
}

/** 完成配送 */
export function completeOrder(ordId) {
  return api.put(`/delivery/emp/${ordId}`)
}

/** 获取我的配送订单 */
export function getMyDeliveries() {
  return api.get('/delivery/emp/my')
}

/** 获取当前登录员工个人信息 */
export function getMyInfo() {
  return api.get('/delivery/emps/me')
}

/** 修改当前登录员工个人信息 */
export function updateMyInfo(data) {
  return api.put('/delivery/emps/me', data)
}