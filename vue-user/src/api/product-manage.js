import api from './index'

/** 查询商铺商品列表 */
export function getProductsByShop(merId) {
  return api.get(`/users/products/merchant/${merId}`)
}

/** 添加商品到指定商铺 */
export function addProduct(merId, { name, describe, img, price, stage }) {
  return api.post(`/users/products/${merId}`, { name, describe, img, price, stage })
}

/** 删除商品 */
export function deleteProduct(id) {
  return api.delete(`/users/products/${id}`)
}

/** 修改商品上下架状态 */
export function updateProductStage(id, stage) {
  return api.put(`/users/products/${id}/stage`, { stage })
}

/** 删除商品图片 */
export function deleteProductImage(id) {
  return api.put(`/users/products/${id}/img`, { img: '' })
}

/** 上传商品图片 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  const token = localStorage.getItem('token')
  return api.post('/users/products/upload', formData, {
    headers: token && token !== 'null' && token !== 'undefined' ? { token } : {},
  })
}