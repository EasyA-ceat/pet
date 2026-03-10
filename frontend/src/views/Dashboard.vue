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
        <button @click="handleQuickAddAppointment" class="btn btn-success">登记预约</button>
        <button @click="handleViewDailyReport" class="btn btn-outline">查看日报表</button>
        <button @click="handleAppointmentManagement" class="btn btn-outline">预约管理</button>
      </div>
    </div>

    <!-- 数据展示区域 -->
    <div class="data-sections">
      <!-- 最近交易 -->
      <div class="data-section">
        <h3>最近交易</h3>
        <el-table :data="recentTransactions" style="width: 100%" size="small">
          <el-table-column prop="customer.customerName" label="顾客" width="100" />
          <el-table-column prop="serviceType" label="服务类型" width="100" />
          <el-table-column prop="amount" label="金额" width="80">
            <template #default="{ row }">
              ¥{{ row.amount?.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="transactionDate" label="时间">
            <template #default="{ row }">
              {{ formatDate(row.transactionDate) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 最近顾客 -->
      <div class="data-section">
        <h3>最近顾客</h3>
        <el-table :data="recentCustomers" style="width: 100%" size="small">
          <el-table-column prop="customerName" label="顾客姓名" width="100" />
          <el-table-column prop="petName" label="宠物姓名" width="100" />
          <el-table-column prop="phone" label="电话" />
        </el-table>
      </div>

      <!-- 今日预约 -->
      <div class="data-section">
        <h3>今日预约</h3>
        <el-table :data="todayAppointments" style="width: 100%" size="small">
          <el-table-column prop="customer.customerName" label="顾客姓名" width="100" />
          <el-table-column prop="customer.petName" label="宠物姓名" width="100" />
          <el-table-column prop="serviceType" label="预约服务" width="100" />
          <el-table-column label="预约时间" width="150">
            <template #default="{ row }">
              {{ formatDateTime(row.appointmentTime) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" size="small">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 新增交易对话框 -->
    <el-dialog v-model="dialogVisible" title="新增交易" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="顾客类型">
          <el-radio-group v-model="customerType">
            <el-radio label="existing">老顾客</el-radio>
            <el-radio label="new">新顾客</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 老顾客选择 -->
        <template v-if="customerType === 'existing'">
          <el-form-item label="选择顾客">
            <el-select v-model="form.customerId" placeholder="请选择顾客" style="width: 100%" filterable>
              <el-option
                v-for="customer in customerList"
                :key="customer.id"
                :label="`${customer.customerName} - ${customer.petName}`"
                :value="customer.id"
              />
            </el-select>
          </el-form-item>
        </template>

        <!-- 新顾客输入 -->
        <template v-if="customerType === 'new'">
          <el-form-item label="顾客姓名">
            <el-input v-model="form.customerName" placeholder="请输入顾客姓名" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="宠物姓名">
            <el-input v-model="form.petName" placeholder="请输入宠物姓名" />
          </el-form-item>
          <el-form-item label="宠物类型">
            <el-input v-model="form.petType" placeholder="例如：狗、猫" />
          </el-form-item>
          <el-form-item label="宠物品种">
            <el-input v-model="form.petBreed" placeholder="请输入宠物品种" />
          </el-form-item>
          <el-form-item label="宠物年龄">
            <el-input-number v-model="form.petAge" :min="0" style="width: 100%" />
          </el-form-item>
        </template>

        <el-form-item label="员工">
          <el-select v-model="form.clerkId" placeholder="请选择员工" style="width: 100%">
            <el-option
              v-for="clerk in clerkList"
              :key="clerk.id"
              :label="clerk.clerkName"
              :value="clerk.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="form.serviceType" placeholder="请选择服务类型" style="width: 100%">
            <el-option label="洗护" value="WASH_CARE" />
            <el-option label="美容" value="BEAUTY" />
            <el-option label="局部修剪" value="PARTIAL_TRIM" />
            <el-option label="寄养" value="FOSTER_CARE" />
            <el-option label="其他服务" value="OTHER_SERVICE" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="form.paymentMethod" placeholder="请选择支付方式" style="width: 100%">
            <el-option label="现金" value="CASH" />
            <el-option label="储值扣款" value="STORED_VALUE" />
            <el-option label="美团团购券" value="MEITUAN" />
            <el-option label="抖音团购券" value="DOUYIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="可用余额" v-if="customerType === 'existing' && form.paymentMethod === 'STORED_VALUE'">
          <span style="font-size: 18px; font-weight: bold; color: #409eff">¥{{ (selectedCustomer?.balance || 0).toFixed(2) }}</span>
          <el-tag v-if="selectedCustomer?.isVip" type="warning" style="margin-left: 10px">VIP</el-tag>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.notes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增预约对话框 -->
    <el-dialog v-model="appointmentDialogVisible" title="登记预约" width="600px">
      <el-form :model="appointmentForm" label-width="120px">
        <el-form-item label="顾客类型">
          <el-radio-group v-model="appointmentCustomerType">
            <el-radio label="existing">老顾客</el-radio>
            <el-radio label="new">新顾客</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 老顾客选择 - 支持搜索 -->
        <template v-if="appointmentCustomerType === 'existing'">
          <el-form-item label="选择顾客">
            <el-select 
              v-model="appointmentForm.customerId" 
              placeholder="请选择或输入顾客名称搜索" 
              style="width: 100%" 
              filterable
              :filter-method="filterCustomer"
            >
              <el-option
                v-for="customer in filteredCustomerList"
                :key="customer.id"
                :label="`${customer.customerName} - ${customer.petName}`"
                :value="customer.id"
              />
            </el-select>
          </el-form-item>
        </template>

        <!-- 新顾客输入 -->
        <template v-if="appointmentCustomerType === 'new'">
          <el-form-item label="顾客姓名">
            <el-input v-model="appointmentForm.customerName" placeholder="请输入顾客姓名" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="appointmentForm.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="宠物姓名">
            <el-input v-model="appointmentForm.petName" placeholder="请输入宠物姓名" />
          </el-form-item>
          <el-form-item label="宠物类型">
            <el-input v-model="appointmentForm.petType" placeholder="例如：狗、猫" />
          </el-form-item>
          <el-form-item label="宠物品种">
            <el-input v-model="appointmentForm.petBreed" placeholder="请输入宠物品种" />
          </el-form-item>
          <el-form-item label="宠物年龄">
            <el-input-number v-model="appointmentForm.petAge" :min="0" style="width: 100%" />
          </el-form-item>
        </template>

        <el-form-item label="预约时间">
          <el-date-picker
            v-model="appointmentForm.appointmentTime"
            type="datetime"
            placeholder="选择预约时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="appointmentForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="appointmentForm.serviceType" placeholder="请选择服务类型" style="width: 100%">
            <el-option label="洗澡" value="洗澡" />
            <el-option label="美容" value="美容" />
            <el-option label="体检" value="体检" />
            <el-option label="寄养" value="寄养" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="appointmentForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待确认" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="appointmentForm.notes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="appointmentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAppointmentSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCustomerList } from '@/api/customer'
import { getClerkList } from '@/api/clerk'
import { getTransactionList } from '@/api/transaction'
import { createTransaction } from '@/api/transaction'
import { createCustomer } from '@/api/customer'
import * as appointmentApi from '@/api/appointment'
import { getDashboardData } from '@/api/dashboard'

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

// 对话框相关
const dialogVisible = ref(false)
const customerType = ref('existing')
const customerList = ref([])
const clerkList = ref([])
const transactionList = ref([])
const appointmentList = ref([])

const form = reactive({
  customerId: null,
  customerName: '',
  phone: '',
  petName: '',
  petType: '',
  petBreed: '',
  petAge: null,
  clerkId: null,
  serviceType: '',
  paymentMethod: 'CASH',
  amount: 0,
  notes: ''
})

const selectedCustomer = computed(() => {
  if (!form.customerId) return null
  return customerList.value.find(c => c.id === form.customerId)
})

// 预约对话框相关
const appointmentDialogVisible = ref(false)
const appointmentCustomerType = ref('existing')
const filteredCustomerList = ref([])

const appointmentForm = reactive({
  customerId: null,
  customerName: '',
  phone: '',
  petName: '',
  petType: '',
  petBreed: '',
  petAge: null,
  appointmentTime: '',
  endTime: '',
  serviceType: '',
  status: 'PENDING',
  notes: ''
})

// 计算最近交易
const recentTransactions = computed(() => {
  return [...transactionList.value]
    .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate))
    .slice(0, 5)
})

// 计算最近顾客
const recentCustomers = computed(() => {
  return [...customerList.value]
    .sort((a, b) => new Date(b.createTime || 0) - new Date(a.createTime || 0))
    .slice(0, 5)
})

// 今日预约
const todayAppointments = computed(() => {
  return appointmentList.value
})

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    CONFIRMED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'info'
  }
  return map[status] || ''
}

const getStatusText = (status) => {
  const map = {
    PENDING: '待确认',
    CONFIRMED: '已确认',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

// 顾客搜索过滤
const filterCustomer = (query) => {
  if (query) {
    filteredCustomerList.value = customerList.value.filter(customer => {
      return customer.customerName.toLowerCase().includes(query.toLowerCase()) ||
             customer.petName.toLowerCase().includes(query.toLowerCase())
    })
  } else {
    filteredCustomerList.value = customerList.value
  }
}

// 加载仪表盘数据
const loadDashboardData = async () => {
  try {
    const data = await getDashboardData()
    
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

// 加载顾客数据
const loadCustomerData = async () => {
  try {
    const data = await getCustomerList()
    customerList.value = data
    filteredCustomerList.value = data
  } catch (error) {
    ElMessage.error('加载顾客数据失败')
  }
}

// 加载员工数据
const loadClerkData = async () => {
  try {
    const data = await getClerkList()
    clerkList.value = data
  } catch (error) {
    ElMessage.error('加载员工数据失败')
  }
}

// 加载交易数据
const loadTransactionData = async () => {
  try {
    const data = await getTransactionList()
    transactionList.value = data
  } catch (error) {
    ElMessage.error('加载交易数据失败')
  }
}

// 加载预约数据
const loadAppointmentData = async () => {
  try {
    const data = await appointmentApi.getTodayAppointments()
    appointmentList.value = data
  } catch (error) {
    console.error('加载预约数据失败')
  }
}

// 快速操作处理函数
const handleQuickAddCustomer = () => {
  router.push('/customers')
}

const handleQuickAddTransaction = () => {
  customerType.value = 'existing'
  Object.assign(form, {
    customerId: null,
    customerName: '',
    phone: '',
    petName: '',
    petType: '',
    petBreed: '',
    petAge: null,
    clerkId: null,
    serviceType: '',
    paymentMethod: 'CASH',
    amount: 0,
    notes: ''
  })
  dialogVisible.value = true
}

const handleQuickAddAppointment = () => {
  appointmentCustomerType.value = 'existing'
  Object.assign(appointmentForm, {
    customerId: null,
    customerName: '',
    phone: '',
    petName: '',
    petType: '',
    petBreed: '',
    petAge: null,
    appointmentTime: '',
    endTime: '',
    serviceType: '',
    status: 'PENDING',
    notes: ''
  })
  appointmentDialogVisible.value = true
}

const handleViewDailyReport = () => {
  router.push('/reports')
}

const handleAppointmentManagement = () => {
  router.push('/appointments')
}

// 提交处理
const handleSubmit = async () => {
  if (customerType.value === 'existing') {
    if (!form.customerId) {
      ElMessage.warning('请选择顾客')
      return
    }
  } else {
    if (!form.customerName) {
      ElMessage.warning('请输入顾客姓名')
      return
    }
    if (!form.phone) {
      ElMessage.warning('请输入联系电话')
      return
    }
    if (!form.petName) {
      ElMessage.warning('请输入宠物姓名')
      return
    }
  }
  
  if (!form.clerkId) {
    ElMessage.warning('请选择员工')
    return
  }
  if (!form.serviceType) {
    ElMessage.warning('请输入服务类型')
    return
  }
  if (!form.amount || form.amount <= 0) {
    ElMessage.warning('请输入金额')
    return
  }
  
  try {
    let customerId = form.customerId
    
    if (customerType.value === 'new') {
      const newCustomer = {
        customerName: form.customerName,
        phone: form.phone,
        petName: form.petName,
        petType: form.petType,
        petBreed: form.petBreed,
        petAge: form.petAge,
        notes: ''
      }
      const createdCustomer = await createCustomer(newCustomer)
      customerId = createdCustomer.id
    }
    
    const clerk = clerkList.value.find(c => c.id === form.clerkId)
    
    const data = {
      customerId: customerId,
      clerkId: form.clerkId,
      serviceType: form.serviceType,
      paymentMethod: form.paymentMethod,
      amount: form.amount,
      commission: clerk && form.amount ?
        form.amount * clerk.commissionRate : 0,
      notes: form.notes
    }
    
    await createTransaction(data)
    ElMessage.success('交易添加成功')
    dialogVisible.value = false
    loadDashboardData()
    loadTransactionData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 预约提交处理
const handleAppointmentSubmit = async () => {
  if (appointmentCustomerType.value === 'existing') {
    if (!appointmentForm.customerId) {
      ElMessage.warning('请选择顾客')
      return
    }
  } else {
    if (!appointmentForm.customerName) {
      ElMessage.warning('请输入顾客姓名')
      return
    }
    if (!appointmentForm.phone) {
      ElMessage.warning('请输入联系电话')
      return
    }
    if (!appointmentForm.petName) {
      ElMessage.warning('请输入宠物姓名')
      return
    }
  }
  
  if (!appointmentForm.appointmentTime) {
    ElMessage.warning('请选择预约时间')
    return
  }
  if (!appointmentForm.serviceType) {
    ElMessage.warning('请选择服务类型')
    return
  }
  
  try {
    let customerId = appointmentForm.customerId
    
    if (appointmentCustomerType.value === 'new') {
      const newCustomer = {
        customerName: appointmentForm.customerName,
        phone: appointmentForm.phone,
        petName: appointmentForm.petName,
        petType: appointmentForm.petType,
        petBreed: appointmentForm.petBreed,
        petAge: appointmentForm.petAge,
        notes: ''
      }
      const createdCustomer = await createCustomer(newCustomer)
      customerId = createdCustomer.id
    }
    
    const data = {
      customerId: customerId,
      appointmentTime: appointmentForm.appointmentTime,
      endTime: appointmentForm.endTime || null,
      serviceType: appointmentForm.serviceType,
      status: appointmentForm.status,
      notes: appointmentForm.notes
    }
    
    await appointmentApi.createAppointment(data)
    ElMessage.success('预约添加成功')
    appointmentDialogVisible.value = false
    loadAppointmentData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadDashboardData()
  loadCustomerData()
  loadClerkData()
  loadTransactionData()
  loadAppointmentData()
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
  background: #409eff;
  transition: width 0.3s;
}

.progress-label {
  text-align: center;
  color: #666;
  font-size: 14px;
}

.quick-actions {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.quick-actions h3 {
  margin-bottom: 15px;
  color: #333;
}

.actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-primary {
  background: #409eff;
  color: white;
}

.btn-primary:hover {
  background: #66b1ff;
}

.btn-secondary {
  background: #67c23a;
  color: white;
}

.btn-secondary:hover {
  background: #85ce61;
}

.btn-success {
  background: #e6a23c;
  color: white;
}

.btn-success:hover {
  background: #ebb563;
}

.btn-outline {
  background: white;
  color: #409eff;
  border: 1px solid #409eff;
}

.btn-outline:hover {
  background: #ecf5ff;
}

.data-sections {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.data-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.data-section h3 {
  margin-bottom: 15px;
  color: #333;
}
</style>
