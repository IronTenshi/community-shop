import api from './index'

/** 分页查询商品（支持按名称模糊搜索） */
export function getProducts({ page = 1, pageSize = 12, name = '' } = {}) {
  return api.post('/productss', { page, pageSize, name: name || undefined })
}