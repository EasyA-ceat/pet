
<template>
  <div class="transactions">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>财务管理</span>
          <el-button type="primary" @click="handleAdd">新增交易</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item>
          <el-input v-model="searchForm.keyword" placeholder="搜索顾客姓名" clearable @clear="handleRefresh" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleRefresh">刷新</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="transactionList" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="顾客姓名" width="120">
          <template #default="{ row }">
            {{ row.customer?.customerName }}
          </template>
        </el-table-column>
        <el-table-column label="宠物姓名" width="120">
          <template #default="{ row }">
            {{ row.customer?.petName }}
          </template>
        </el-table-column>
        <el-table-column label="员工姓名" width="120">
          <template #default="{ row }">
            {{ row.clerk?.clerkName }}
          </template>
        </el-table-column>
        <el-table-column prop="serviceType" label="服务类型" width="150" />
        <el-table-column prop="transactionDate" label="交易时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.transactionDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="commission" label="提成" width="120">
          <template #default="{ row }">
            ¥{{ row.commission?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="stats">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="交易总数">{{ stats.transactionCount }}</el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ stats.totalAmount?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="总提成">¥{{ stats.totalCommission?.toFixed(2) }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="transactionForm" :rules="rules" ref="transactionFormRef" label-width="100px">
        <el-form-item label="顾客" prop="customerId">
          <el-select v-model="transactionForm.customerId" placeholder="请选择顾客" style="width: 100%">
            <el-option
              v-for="customer in customerList"
              :key="customer.id"
              :label="`${customer.customerName} - ${customer.petName}`"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="员工" prop="clerkId">
          <el-select v-model="transactionForm.clerkId" placeholder="请选择员工" style="width: 100%">
            <el-option
              v-for="clerk in clerkList"
              :key="clerk.id"
              :label="clerk.clerkName"
              :value="clerk.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-input v-model="transactionForm.serviceType" placeholder="请输入服务类型" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="transactionForm.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="transactionForm.notes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTransactionList, createTransaction, updateTransaction, deleteTransaction, searchTransactions } from '@/api/transaction'
import { getCustomerList } from '@/api/customer'
import { getClerkList } from '@/api/clerk'

const transactionList = ref([])
const allTransactionList = ref([])
const customerList = ref([])
const clerkList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加交易')
const transactionFormRef = ref(null)

const searchForm = reactive({
  keyword: ''
})

const transactionForm = reactive({
  id: null,
  customerId: null,
  clerkId: null,
  serviceType: '',
  amount: 0,
  notes: ''
})

const stats = computed(() => {
  let transactionCount = 0
  let totalAmount = 0
  let totalCommission = 0

  transactionList.value.forEach(t => {
    transactionCount++
    totalAmount += t.amount || 0
    totalCommission += t.commission || 0
  })

  return {
    transactionCount,
    totalAmount,
    totalCommission
  }
})

const rules = {
  customerId: [{ required: true, message: '请选择顾客', trigger: 'change' }],
  clerkId: [{ required: true, message: '请选择员工', trigger: 'change' }],
  serviceType: [{ required: true, message: '请输入服务类型', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }]
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const loadTransactionData = async () => {
  try {
    const data = await getTransactionList()
    allTransactionList.value = data
    transactionList.value = data
  } catch (error) {
    ElMessage.error('加载交易数据失败')
  }
}

const loadCustomerData = async () => {
  try {
    const data = await getCustomerList()
    customerList.value = data
  } catch (error) {
    ElMessage.error('加载顾客数据失败')
  }
}

const loadClerkData = async () => {
  try {
    const data = await getClerkList()
    clerkList.value = data
  } catch (error) {
    ElMessage.error('加载员工数据失败')
  }
}

const handleSearch = async () => {
  if (!searchForm.keyword || searchForm.keyword.trim() === '') {
    transactionList.value = allTransactionList.value
    return
  }
  
  try {
    const data = await searchTransactions(searchForm.keyword)
    transactionList.value = data
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const handleRefresh = () => {
  searchForm.keyword = ''
  transactionList.value = allTransactionList.value
}

const handleAdd = () => {
  dialogTitle.value = '添加交易'
  Object.assign(transactionForm, {
    id: null,
    customerId: null,
    clerkId: null,
    serviceType: '',
    amount: 0,
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑交易'
  Object.assign(transactionForm, {
    id: row.id,
    customerId: row.customer?.id,
    clerkId: row.clerk?.id,
    serviceType: row.serviceType,
    amount: row.amount,
    notes: row.notes
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!transactionFormRef.value) return
  
  await transactionFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const customer = customerList.value.find(c => c.id === transactionForm.customerId)
        const clerk = clerkList.value.find(c => c.id === transactionForm.clerkId)
        
        const data = {
          ...transactionForm,
          customer,
          clerk,
          commission: clerk && transactionForm.amount ? 
            transactionForm.amount * clerk.commissionRate : 0
        }

        if (transactionForm.id) {
          await updateTransaction(transactionForm.id, data)
          ElMessage.success('交易更新成功')
        } else {
          await createTransaction(data)
          ElMessage.success('交易添加成功')
        }
        dialogVisible.value = false
        loadTransactionData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该交易吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteTransaction(row.id)
    ElMessage.success('交易删除成功')
    loadTransactionData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadTransactionData()
  loadCustomerData()
  loadClerkData()
})
</script>

<style scoped>
.transactions {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.stats {
  margin-top: 20px;
}
</style>
