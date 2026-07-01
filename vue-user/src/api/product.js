import api from './index'

/** 分页查询商品（支持按名称模糊搜索） */
export function getProducts({ page = 1, pageSize = 12, name = '' } = {}) {
  return api.post('/productss', { page, pageSize, name: name || undefined })
}

/** 根据商铺ID查询该商铺下的所有商品 */
export function getShopProducts(merId) {
  return api.post(`/productss/merchant/${merId}`)
}