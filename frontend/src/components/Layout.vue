<template>
  <el-container class="layout-container">
    <el-aside width="240px" class="sidebar">
      <div class="logo">
        <h2>宠物管理系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/" v-if="isAdmin() || hasPermission('DASHBOARD')">
          <el-icon><Odometer /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/customers" v-if="isAdmin() || hasPermission('CUSTOMER')">
          <el-icon><User /></el-icon>
          <span>顾客管理</span>
        </el-menu-item>
        <el-menu-item index="/appointments" v-if="isAdmin() || hasPermission('APPOINTMENT')">
          <el-icon><Calendar /></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/transactions" v-if="isAdmin() || hasPermission('TRANSACTION')">
          <el-icon><Document /></el-icon>
          <span>财务管理</span>
        </el-menu-item>
        <el-menu-item index="/recharge" v-if="isAdmin() || hasPermission('RECHARGE')">
          <el-icon><Wallet /></el-icon>
          <span>储值管理</span>
        </el-menu-item>
        <el-menu-item index="/clerks" v-if="isAdmin() || hasPermission('CLERK')">
          <el-icon><Avatar /></el-icon>
          <span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/roles" v-if="isAdmin() || hasPermission('ROLE')">
          <el-icon><Key /></el-icon>
          <span>角色权限</span>
        </el-menu-item>
        <el-menu-item index="/reports" v-if="isAdmin() || hasPermission('REPORT')">
          <el-icon><DataAnalysis /></el-icon>
          <span>报表中心</span>
        </el-menu-item>
        <el-menu-item index="/settings" v-if="isAdmin() || hasPermission('SETTINGS')">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
        <el-menu-item index="/about">
          <el-icon><InfoFilled /></el-icon>
          <span>关于</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="main-container">
      <el-header class="header">
        <div class="header-left">
          <span class="page-title">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <span class="current-time">{{ currentTime }}</span>
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-icon><User /></el-icon>
              <div class="user-details">
                <span class="user-name">{{ userInfo?.clerkName || userInfo?.username || '用户' }}</span>
                <span class="user-role">{{ getRoleDisplayName(userInfo?.role) }}</span>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Odometer,
  User,
  Calendar,
  Document,
  Wallet,
  Avatar,
  Key,
  DataAnalysis,
  Setting,
  InfoFilled,
  SwitchButton
} from '@element-plus/icons-vue'
import { logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const currentTime = ref('')
let timer = null

const userInfo = computed(() => {
  try {
    const info = localStorage.getItem('userInfo')
    return info ? JSON.parse(info) : null
  } catch {
    return null
  }
})

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const titles = {
    '/': '仪表盘',
    '/customers': '顾客管理',
    '/appointments': '预约管理',
    '/transactions': '财务管理',
    '/recharge': '储值管理',
    '/clerks': '员工管理',
    '/roles': '角色权限',
    '/reports': '报表中心',
    '/settings': '系统设置',
    '/about': '关于'
  }
  return titles[route.path] || '宠物管理系统'
})

// 角色权限映射
const rolePermissions = {
  'ADMIN': ['DASHBOARD', 'CUSTOMER', 'APPOINTMENT', 'TRANSACTION', 'RECHARGE', 'CLERK', 'ROLE', 'REPORT', 'SETTINGS'],
  'MANAGER': ['DASHBOARD', 'CUSTOMER', 'APPOINTMENT', 'TRANSACTION', 'RECHARGE', 'CLERK', 'REPORT'],
  'STAFF': ['DASHBOARD', 'CUSTOMER', 'APPOINTMENT', 'TRANSACTION', 'RECHARGE']
}

const isAdmin = () => {
  return userInfo.value?.role === 'ADMIN'
}

const hasPermission = (permission) => {
  const role = userInfo.value?.role
  if (!role) return false
  const permissions = rolePermissions[role] || []
  return permissions.includes(permission)
}

const getRoleDisplayName = (role) => {
  const roleMap = {
    'ADMIN': '管理员',
    'MANAGER': '经理',
    'STAFF': '员工'
  }
  return roleMap[role] || '未知'
}

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    weekday: 'long',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch {
      // 用户取消
    }
  }
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
}

.logo h2 {
  color: white;
  margin: 0;
  font-size: 18px;
}

.main-container {
  background-color: #f0f2f5;
}

.header {
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left .page-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-right .current-time {
  color: #666;
  font-size: 14px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.user-role {
  color: #999;
  font-size: 12px;
}

.main-content {
  padding: 20px;
  overflow-y: auto;
}
</style>
