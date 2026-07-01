import api from './index'

/** 分页查询商铺（支持按名称模糊搜索） */
export function getShops({ page = 1, pageSize = 12, name = '' } = {}) {
  return api.post('/shopss', { page, pageSize, name: name || undefined })
}