<script setup>
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <div class="center-page">
    <h2 class="page-title">用户中心</h2>

    <div class="info-card">
      <div class="info-row">
        <span class="label">用户名</span>
        <span class="value">{{ auth.username || '-' }}</span>
      </div>
      <div class="info-row">
        <span class="label">用户 ID</span>
        <span class="value">{{ auth.userInfo?.id || '-' }}</span>
      </div>
      <div class="info-row">
        <span class="label">身份</span>
        <span class="value">普通用户</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.center-page {
}

.page-title {
  font-size: 18px;
  color: #e0e0e0;
  font-weight: 500;
  letter-spacing: 1px;
  margin-bottom: 24px;
}

.info-card {
  background: #1e1e1e;
  border: 1px solid #2a2a2a;
  padding: 24px;
}

.info-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #222;
  font-size: 14px;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  color: #666;
  width: 100px;
  flex-shrink: 0;
}

.value {
  color: #ccc;
}
</style>