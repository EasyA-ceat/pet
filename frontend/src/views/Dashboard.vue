<template>
  <div class="dashboard">
    <h1>宠物管理系统</h1>
    <div class="dashboard-cards">
      <div class="card">
        <h3>今日营收</h3>
        <p class="value">{{ todayRevenue }}</p>
        <p class="trend">{{ todayRevenueTrend }}</p>
      </div>
      <div class="card">
        <h3>活跃顾客</h3>
        <p class="value">{{ activeCustomers }}</p>
        <p class="trend">{{ activeCustomersTrend }}</p>
      </div>
      <div class="card">
        <h3>待办任务</h3>
        <p class="value">{{ pendingTasks }}</p>
      </div>
      <div class="card">
        <h3>月度目标</h3>
        <p class="value">{{ monthlyTarget }}</p>
        <p class="trend">{{ monthlyTargetProgress }}</p>
      </div>
    </div>
    <div class="progress-section">
      <h3>本周目标完成度</h3>
      <div class="progress-bar">
        <div class="progress" :style="{ width: weeklyProgress + '%' }"></div>
      </div>
      <p class="progress-label">{{ weeklyProgressLabel }}</p>
    </div>
    <div class="quick-actions">
      <h3>快速操作</h3>
      <div class="actions">
        <button @click="handleQuickAddCustomer" class="btn btn-primary">登记新顾客</button>
        <button @click="handleQuickAddTransaction" class="btn btn-secondary">记录新交易</button>
        <button @click="handleViewDailyReport" class="btn btn-outline">查看日报表</button>
        <button @click="handleAppointmentManagement" class="btn btn-outline">预约管理</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// 数据状态
const todayRevenue = ref('¥0.00')
const todayRevenueTrend = ref('')
const activeCustomers = ref('0')
const activeCustomersTrend = ref('')
const pendingTasks = ref('0')
const monthlyTarget = ref('¥0.00')
const monthlyTargetProgress = ref('')
const weeklyProgress = ref(0)
const weeklyProgressLabel = ref('0% (0/5000)')

// 加载仪表盘数据
const loadDashboardData = async () => {
  try {
    const response = await axios.get('/api/dashboard')
    const data = response.data
    
    todayRevenue.value = data.todayRevenue
    todayRevenueTrend.value = data.todayRevenueTrend
    activeCustomers.value = data.activeCustomers
    activeCustomersTrend.value = data.activeCustomersTrend
    pendingTasks.value = data.pendingTasks
    monthlyTarget.value = data.monthlyTarget
    monthlyTargetProgress.value = data.monthlyTargetProgress
    weeklyProgress.value = data.weeklyProgress
    weeklyProgressLabel.value = data.weeklyProgressLabel
  } catch (error) {
    console.error('加载仪表盘数据失败:', error)
  }
}

// 快速操作处理函数
const handleQuickAddCustomer = () => {
  router.push('/customers')
}

const handleQuickAddTransaction = () => {
  router.push('/transactions')
}

const handleViewDailyReport = () => {
  router.push('/reports')
}

const handleAppointmentManagement = () => {
  alert('预约管理功能开发中...')
}

// 组件挂载时加载数据
onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  margin-bottom: 30px;
  color: #333;
}

.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card h3 {
  margin-bottom: 10px;
  color: #666;
  font-size: 14px;
}

.card .value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.card .trend {
  font-size: 12px;
  color: #999;
}

.progress-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.progress-section h3 {
  margin-bottom: 15px;
  color: #333;
}

.progress-bar {
  width: 100%;
  height: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 10px;
}

.progress {
  height: 100%;
  background: #4CAF50;
  border-radius: 10px;
  transition: width 0.3s ease;
}

.progress-label {
  font-size: 14px;
  color: #666;
  text-align: right;
}

.quick-actions {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quick-actions h3 {
  margin-bottom: 15px;
  color: #333;
}

.actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
}

.btn {
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.btn-primary {
  background: #4CAF50;
  color: white;
}

.btn-secondary {
  background: #2196F3;
  color: white;
}

.btn-outline {
  background: white;
  color: #333;
  border: 1px solid #ddd;
}
</style>