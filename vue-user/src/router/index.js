import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue'),
    },
    {
      path: '/',
      component: () => import('@/layouts/UserLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/products',
        },
        {
          path: 'products',
          name: 'products',
          component: () => import('@/views/Products.vue'),
        },
        {
          path: 'shops',
          name: 'shops',
          component: () => import('@/views/Shops.vue'),
        },
        {
          path: 'shops/:id',
          name: 'shop-detail',
          component: () => import('@/views/ShopDetail.vue'),
        },
        {
          path: 'cart',
          name: 'cart',
          component: () => import('@/views/Cart.vue'),
        },
        {
          path: 'orders',
          name: 'orders',
          component: () => import('@/views/Orders.vue'),
        },
        {
          path: 'center',
          name: 'center',
          component: () => import('@/views/Center.vue'),
        },
        {
          path: 'merchant/shops',
          name: 'merchant-shops',
          component: () => import('@/views/merchant/MyShops.vue'),
        },
        {
          path: 'merchant/shops/:id',
          name: 'merchant-shop-detail',
          component: () => import('@/views/merchant/ShopProducts.vue'),
        },
      ],
    },
  ],
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const validToken = token && token !== 'null' && token !== 'undefined'
  if (to.meta.requiresAuth && !validToken) {
    return next('/login')
  }
  if (validToken && to.path === '/login') {
    return next('/products')
  }
  next()
})

export default router