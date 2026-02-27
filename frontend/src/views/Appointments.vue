<template>
  <div class="appointments">
    <el-card class="stats-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="总预约数" :value="stats.totalAppointments || 0">
            <template #suffix>个</template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="待确认" :value="stats.pendingCount || 0">
            <template #suffix>个</template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="已确认" :value="stats.confirmedCount || 0">
            <template #suffix>个</template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="今日预约" :value="stats.todayCount || 0">
            <template #suffix>个</template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
          <div>
            <el-button-group>
              <el-button type="primary" :icon="Calendar" @click="viewToday">今日</el-button>
              <el-button type="primary" :icon="Clock" @click="viewUpcoming">即将到来</el-button>
              <el-button type="primary" :icon="Plus" @click="addAppointment">添加预约</el-button>
            </el-button-group>
          </div>
        </div>
      </template>

      <el-table :data="appointments" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="customer.customerName" label="顾客姓名" width="120" />
        <el-table-column prop="customer.petName" label="宠物姓名" width="120" />
        <el-table-column label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.appointmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="serviceType" label="服务类型" width="150" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="editAppointment(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteAppointment(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑预约对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑预约' : '添加预约'"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="选择顾客" prop="customerId">
          <el-select v-model="form.customerId" placeholder="请选择顾客" style="width: 100%">
            <el-option
              v-for="customer in customers"
              :key="customer.id"
              :label="`${customer.customerName} - ${customer.petName}`"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            v-model="form.appointmentTime"
            type="datetime"
            placeholder="选择预约时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="form.serviceType" placeholder="请选择服务类型" style="width: 100%">
            <el-option label="洗澡" value="洗澡" />
            <el-option label="美容" value="美容" />
            <el-option label="体检" value="体检" />
            <el-option label="寄养" value="寄养" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待确认" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="form.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calendar, Clock, Plus } from '@element-plus/icons-vue'
import * as appointmentApi from '@/api/appointment'
import * as customerApi from '@/api/customer'

const appointments = ref([])
const customers = ref([])
const stats = ref({})
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = ref({
  id: null,
  customerId: null,
  appointmentTime: '',
  endTime: '',
  serviceType: '',
  status: 'PENDING',
  notes: ''
})

const rules = {
  customerId: [{ required: true, message: '请选择顾客', trigger: 'change' }],
  appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
  serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }]
}

const loadAppointments = async () => {
  try {
    const data = await appointmentApi.getAppointmentList()
    appointments.value = data
  } catch (error) {
    ElMessage.error('加载预约列表失败')
    console.error(error)
  }
}

const loadCustomers = async () => {
  try {
    const data = await customerApi.getCustomerList()
    customers.value = data
  } catch (error) {
    console.error(error)
  }
}

const loadStats = async () => {
  try {
    const data = await appointmentApi.getAppointmentStats()
    stats.value = data
  } catch (error) {
    console.error(error)
  }
}

const viewToday = async () => {
  try {
    const data = await appointmentApi.getTodayAppointments()
    appointments.value = data
  } catch (error) {
    ElMessage.error('加载今日预约失败')
    console.error(error)
  }
}

const viewUpcoming = async () => {
  try {
    const data = await appointmentApi.getUpcomingAppointments()
    appointments.value = data
  } catch (error) {
    ElMessage.error('加载即将到来的预约失败')
    console.error(error)
  }
}

const addAppointment = () => {
  isEdit.value = false
  form.value = {
    id: null,
    customerId: null,
    appointmentTime: '',
    endTime: '',
    serviceType: '',
    status: 'PENDING',
    notes: ''
  }
  dialogVisible.value = true
}

const editAppointment = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    customerId: row.customer.id,
    appointmentTime: row.appointmentTime,
    endTime: row.endTime || '',
    serviceType: row.serviceType,
    status: row.status,
    notes: row.notes || ''
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await appointmentApi.updateAppointment(form.value.id, form.value)
          ElMessage.success('更新预约成功')
        } else {
          await appointmentApi.createAppointment(form.value)
          ElMessage.success('创建预约成功')
        }
        dialogVisible.value = false
        await loadAppointments()
        await loadStats()
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新预约失败' : '创建预约失败')
        console.error(error)
      }
    }
  })
}

const deleteAppointment = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await appointmentApi.deleteAppointment(id)
    ElMessage.success('删除预约成功')
    await loadAppointments()
    await loadStats()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除预约失败')
      console.error(error)
    }
  }
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

onMounted(() => {
  loadAppointments()
  loadCustomers()
  loadStats()
})
</script>

<style scoped>
.appointments {
  max-width: 1600px;
  margin: 0 auto;
}

.stats-card {
  margin-bottom: 20px;
}

.main-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
