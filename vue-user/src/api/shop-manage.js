import api from './index'

/** 注册商铺 */
export function registerShop({ name, phone, person }) {
  return api.post('/shops/register', { name, phone, person })
}

/** 修改商铺信息 */
export function updateShop({ id, name, phone, person }) {
  return api.put('/shops/update', { id, name, phone, person })
}

/** 删除商铺 */
export function deleteShop(id) {
  return api.delete(`/shops/${id}`)
}

/** 查询我的商铺列表 */
export function getMyShops() {
  return api.get('/shops')
}