<template>
  <div class="recharge-records">
    <div class="toolbar">
      <el-select v-model="searchForm.customerId" placeholder="选择顾客" clearable style="width: 250px; margin-right: 10px" @clear="handleRefresh">
        <el-option
          v-for="customer in customerList"
          :key="customer.id"
          :label="`${customer.customerName} - ${customer.petName}`"
          :value="customer.id"
        />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleRefresh">刷新</el-button>
      <el-button type="success" @click="handleRecharge">会员充值</el-button>
    </div>

    <el-table :data="recordList" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="顾客" width="180">
        <template #default="{ row }">
          {{ row.customer?.customerName }} - {{ row.customer?.petName }}
        </template>
      </el-table-column>
      <el-table-column label="充值金额" width="130">
        <template #default="{ row }">
          <span style="color: #67c23a; font-weight: bold">+¥{{ row.amount?.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="赠送金额" width="130">
        <template #default="{ row }">
          <span v-if="row.bonusAmount > 0" style="color: #e6a23c">+¥{{ row.bonusAmount?.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ getPaymentMethodDisplayName(row.paymentMethod) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="rechargeTime" label="充值时间" width="180">
        <template #default="{ row }">
          {{ formatDateTime(row.rechargeTime) }}
        </template>
      </el-table-column>
      <el-table-column label="活动" width="150">
        <template #default="{ row }">
          <el-tag v-if="row.activity" type="success" size="small">{{ row.activity.name }}</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="notes" label="备注" />
    </el-table>

    <!-- 充值对话框 -->
    <el-dialog v-model="rechargeDialogVisible" title="会员充值" width="600px">
      <el-form :model="rechargeForm" :rules="rechargeRules" ref="rechargeFormRef" label-width="120px">
        <el-form-item label="选择会员" prop="customerId">
          <el-select v-model="rechargeForm.customerId" placeholder="请选择会员" style="width: 100%">
            <el-option
              v-for="customer in customerList"
              :key="customer.id"
              :label="`${customer.customerName} - ${customer.petName}`"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="当前余额" v-if="selectedCustomer">
          <div style="display: flex; align-items: center; gap: 10px;">
            <span style="font-size: 24px; font-weight: bold; color: #409eff">¥{{ selectedCustomer.balance?.toFixed(2) || '0.00' }}</span>
            <el-tag v-if="selectedCustomer.isVip" type="warning">VIP会员</el-tag>
          </div>
        </el-form-item>
        <el-form-item label="充值金额" prop="amount">
          <el-input-number v-model="rechargeForm.amount" :min="0" :precision="2" style="width: 100%" @change="calculateBonus" />
        </el-form-item>
        <el-form-item label="可用活动">
          <div v-if="activeActivities.length > 0" class="activity-list">
            <div v-for="activity in activeActivities" :key="activity.id" class="activity-item">
              <el-tag type="success">充{{ activity.rechargeAmount }}送{{ activity.bonusAmount }}</el-tag>
              <span class="activity-name">{{ activity.name }}</span>
            </div>
          </div>
          <span v-else style="color: #909399">暂无可用活动</span>
        </el-form-item>
        <el-form-item label="预计赠送" v-if="calculatedBonus >= 0">
          <span style="color: #e6a23c; font-weight: bold">+¥{{ calculatedBonus.toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="rechargeForm.paymentMethod" placeholder="请选择支付方式" style="width: 100%">
            <el-option
              v-for="method in paymentMethods"
              :key="method.value"
              :label="method.label"
              :value="method.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="rechargeForm.notes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRechargeSubmit">确定充值</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRechargeRecordList, getActiveRechargeActivities, recharge } from '@/api/recharge'
import { getCustomerList } from '@/api/customer'
import { getPaymentMethods } from '@/api/common'

const recordList = ref([])
const customerList = ref([])
const activeActivities = ref([])
const paymentMethods = ref([])
const rechargeDialogVisible = ref(false)
const rechargeFormRef = ref(null)

const searchForm = reactive({
  customerId: null
})

const rechargeForm = reactive({
  customerId: null,
  amount: 0,
  paymentMethod: 'CASH',
  notes: ''
})

const rechargeRules = {
  customerId: [{ required: true, message: '请选择会员', trigger: 'change' }],
  amount: [{ required: true, message: '请输入充值金额', trigger: 'blur' }],
  paymentMethod: [{ required: true, message: '请选择支付方式', trigger: 'change' }]
}

const selectedCustomer = computed(() => {
  if (!rechargeForm.customerId) return null
  return customerList.value.find(c => c.id === rechargeForm.customerId)
})

const calculatedBonus = ref(0)

const getPaymentMethodDisplayName = (method) => {
  const methodMap = {
    'CASH': '现金',
    'STORED_VALUE': '储值扣款',
    'MEITUAN': '美团团购券',
    'DOUYIN': '抖音团购券'
  }
  return methodMap[method] || method
}

const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const loadRecords = async () => {
  try {
    const data = await getRechargeRecordList()
    recordList.value = data
  } catch (error) {
    ElMessage.error('加载充值记录失败')
  }
}

const loadCustomers = async () => {
  try {
    const data = await getCustomerList()
    customerList.value = data
  } catch (error) {
    ElMessage.error('加载顾客数据失败')
  }
}

const loadActiveActivities = async () => {
  try {
    const data = await getActiveRechargeActivities()
    activeActivities.value = data
  } catch (error) {
    console.error('加载活动失败:', error)
    activeActivities.value = []
  }
}

const loadPaymentMethods = async () => {
  try {
    const data = await getPaymentMethods()
    paymentMethods.value = data
  } catch (error) {
    console.error('加载支付方式失败:', error)
    paymentMethods.value = [
      { value: 'CASH', label: '现金' },
      { value: 'STORED_VALUE', label: '储值扣款' },
      { value: 'MEITUAN', label: '美团团购券' },
      { value: 'DOUYIN', label: '抖音团购券' }
    ]
  }
}

const calculateBonus = () => {
  calculatedBonus.value = 0
  if (!rechargeForm.amount || activeActivities.value.length === 0) return

  // 找到匹配的活动
  for (const activity of activeActivities.value) {
    if (rechargeForm.amount >= activity.rechargeAmount) {
      calculatedBonus.value = activity.bonusAmount
      break
    }
  }
}

const handleSearch = async () => {
  try {
    let data
    if (searchForm.customerId) {
      // TODO: 实现按顾客筛选的API
      data = await getRechargeRecordList()
      data = data.filter(r => r.customer?.id === searchForm.customerId)
    } else {
      data = await getRechargeRecordList()
    }
    recordList.value = data
  } catch (error) {
    ElMessage.error('查询失败')
  }
}

const handleRefresh = () => {
  searchForm.customerId = null
  loadRecords()
}

const handleRecharge = () => {
  Object.assign(rechargeForm, {
    customerId: null,
    amount: 0,
    paymentMethod: 'CASH',
    notes: ''
  })
  calculatedBonus.value = 0
  rechargeDialogVisible.value = true
}

const handleRechargeSubmit = async () => {
  if (!rechargeFormRef.value) return

  await rechargeFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await recharge(rechargeForm)
        ElMessage.success('充值成功')
        rechargeDialogVisible.value = false
        loadRecords()
        loadCustomers()
      } catch (error) {
        ElMessage.error('充值失败')
      }
    }
  })
}

onMounted(() => {
  loadRecords()
  loadCustomers()
  loadActiveActivities()
  loadPaymentMethods()
})
</script>

<style scoped>
.recharge-records {
  padding: 10px 0;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
}

.activity-name {
  color: #606266;
}
</style>
