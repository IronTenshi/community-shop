import api from './index'

/** 新增员工 */
export function createEmployee(data) {
  return api.post('/admin/emps', data)
}

/** 查询员工详情 */
export function getEmployee(id) {
  return api.get(`/admin/emps/${id}`)
}

/** 修改员工信息 */
export function updateEmployee(data) {
  return api.put('/admin/emps', data)
}

/** 删除员工 */
export function deleteEmployee(id) {
  return api.delete(`/admin/emps/${id}`)
}

/** 分页查询员工列表 */
export function listEmployees(params) {
  return api.post('/emp', params)
}

/** 查看所有订单 */
export function getAllOrders() {
  return api.get('/delivery/admin/list')
}

/** 查看订单详情及配送信息 */
export function getOrderDetail(id) {
  return api.get(`/delivery/admin/${id}`)
}