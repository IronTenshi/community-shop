import api from './index'

/** 下单 */
export function createOrder({ reAddress, orderItems }) {
  return api.post('/users/orders', { reAddress, orderItems })
}

/** 查询我的订单 */
export function getMyOrders() {
  return api.get('/users/orders')
}

/** 取消订单 */
export function cancelOrder(id) {
  return api.delete('/users/orders/' + id)
}