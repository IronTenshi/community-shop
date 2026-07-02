import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/',
    component: () => import('@/layouts/EmployeeLayout.vue'),
    meta: { requiresAuth: true },
    redirect: '/delivery/orders',
    children: [
      // 配送员
      {
        path: 'delivery/orders',
        name: 'delivery-orders',
        component: () => import('@/views/delivery/AvailableOrders.vue'),
      },
      {
        path: 'delivery/my',
        name: 'delivery-my',
        component: () => import('@/views/delivery/MyDeliveries.vue'),
      },
      // 个人中心
      {
        path: 'profile',
        name: 'profile',
        component: () => import('@/views/Profile.vue'),
      },
      // 管理员
      {
        path: 'admin/orders',
        name: 'admin-orders',
        component: () => import('@/views/admin/AllOrders.vue'),
      },
      {
        path: 'admin/employees',
        name: 'admin-employees',
        component: () => import('@/views/admin/Employees.vue'),
      },
      {
        path: 'admin/users',
        name: 'admin-users',
        component: () => import('@/views/admin/Users.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory('/employee/'),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('empToken')
  const validToken = token && token !== 'null' && token !== 'undefined'
  if (to.meta.requiresAuth && !validToken) {
    return next('/login')
  }
  if (validToken && to.path === '/login') {
    return next('/delivery/orders')
  }
  next()
})

export default router