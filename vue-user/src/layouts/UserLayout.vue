<script setup>
import { RouterView, useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const cart = useCartStore()

const menuItems = [
  { path: '/products', label: '商品选购', icon: '📦' },
  { path: '/shops', label: '商铺浏览', icon: '🏪' },
  { path: '/cart', label: '购物车', icon: '🛒', badge: () => cart.totalCount },
  { path: '/orders', label: '我的订单', icon: '📋' },
  { path: '/center', label: '用户中心', icon: '⚙' },
]

const merchantItems = [
  { path: '/merchant/shops', label: '我的商铺', icon: '🏬' },
]

function handleLogout() {
  auth.logout()
  cart.clearCart()
  router.push('/login')
}
</script>

<template>
  <div class="layout">
    <!-- 左侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-brand">
        <h2>社区商店</h2>
        <p class="username">{{ auth.username }}</p>
      </div>

      <nav class="sidebar-nav">
        <RouterLink
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path || route.path.startsWith(item.path + '/') }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-label">{{ item.label }}</span>
          <span v-if="item.badge?.()" class="nav-badge">{{ item.badge() }}</span>
        </RouterLink>

        <div class="nav-divider"></div>
        <div class="nav-group-title">商户管理</div>
        <RouterLink
          v-for="item in merchantItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path || route.path.startsWith(item.path + '/') }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </RouterLink>
      </nav>

      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </aside>

    <!-- 右侧内容区 -->
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #1a1a1a;
}

/* ===== 侧边栏 ===== */
.sidebar {
  width: 220px;
  background: #141414;
  border-right: 1px solid #2a2a2a;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.sidebar-brand {
  padding: 24px 20px;
  border-bottom: 1px solid #2a2a2a;
}

.sidebar-brand h2 {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 600;
  letter-spacing: 1px;
}

.username {
  font-size: 12px;
  color: #666;
  margin-top: 6px;
}

/* 导航 */
.sidebar-nav {
  flex: 1;
  padding: 12px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #888;
  text-decoration: none;
  font-size: 14px;
  border-left: 3px solid transparent;
  transition: all 0.15s;
  position: relative;
}

.nav-item:hover {
  color: #ccc;
  background: #1e1e1e;
  border-left-color: #444;
}

.nav-item.active {
  color: #e0e0e0;
  background: #1e1e1e;
  border-left-color: #d4a853;
}

.nav-icon {
  margin-right: 10px;
  font-size: 16px;
}

.nav-label {
  flex: 1;
}

.nav-badge {
  background: #c0392b;
  color: #fff;
  font-size: 11px;
  padding: 1px 7px;
  border-radius: 10px;
  min-width: 20px;
  text-align: center;
}

.nav-divider {
  height: 1px;
  background: #2a2a2a;
  margin: 8px 16px;
}

.nav-group-title {
  font-size: 11px;
  color: #555;
  padding: 8px 20px 4px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* 底部 */
.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid #2a2a2a;
}

.logout-btn {
  width: 100%;
  padding: 8px 0;
  background: transparent;
  color: #888;
  border: 1px solid #333;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.logout-btn:hover {
  color: #c0392b;
  border-color: #c0392b;
  background: #1a0a0a;
}

/* ===== 内容区 ===== */
.content {
  flex: 1;
  padding: 28px 32px;
  overflow-y: auto;
}
</style>