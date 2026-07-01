<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const menuItems = [
  { path: '/delivery/orders', label: '空闲订单', icon: '📋', roles: [0, 1] },
  { path: '/delivery/my', label: '我的配送', icon: '🚚', roles: [1] },
  { path: '/admin/orders', label: '全部订单', icon: '📊', roles: [0] },
  { path: '/admin/employees', label: '员工管理', icon: '👥', roles: [0] },
  { path: '/profile', label: '个人中心', icon: '👤', roles: [0, 1] },
]

function filteredMenu() {
  return menuItems.filter(m => m.roles.includes(auth.userInfo?.job))
}

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>社区商店 · 员工</h2>
        <p class="role-tag">{{ auth.isAdmin ? '管理员' : '配送员' }}</p>
      </div>

      <nav class="sidebar-nav">
        <RouterLink
          v-for="item in filteredMenu()"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </RouterLink>
      </nav>

      <div class="sidebar-footer">
        <div class="user-info">
          <span class="username">{{ auth.userInfo?.username }}</span>
        </div>
        <button class="btn-logout" @click="handleLogout">退出登录</button>
      </div>
    </aside>

    <main class="main">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #0d0d0d;
}

.sidebar {
  width: 220px;
  background: #141414;
  border-right: 1px solid #2a2a2a;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.sidebar-header {
  padding: 20px 16px 16px;
  border-bottom: 1px solid #2a2a2a;
}

.sidebar-header h2 {
  font-size: 15px;
  color: #d4a853;
  margin-bottom: 4px;
}

.role-tag {
  font-size: 11px;
  color: #555;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.sidebar-nav {
  flex: 1;
  padding: 8px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  color: #666;
  font-size: 13px;
  text-decoration: none;
  border-left: 2px solid transparent;
  transition: all 0.15s;
}

.nav-item:hover {
  color: #999;
  background: #1a1a1a;
}

.nav-item.active {
  color: #d4a853;
  background: #1a1a1a;
  border-left-color: #d4a853;
}

.nav-icon {
  margin-right: 10px;
  font-size: 16px;
}

.nav-label {
  flex: 1;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid #2a2a2a;
}

.user-info {
  margin-bottom: 10px;
}

.username {
  font-size: 13px;
  color: #888;
}

.btn-logout {
  width: 100%;
  padding: 8px;
  background: transparent;
  color: #666;
  border: 1px solid #2a2a2a;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-logout:hover {
  color: #c0392b;
  border-color: #c0392b;
}

.main {
  flex: 1;
  padding: 28px 32px;
  overflow-y: auto;
}
</style>