<template>
  <div class="clerk-commission-config">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工服务抽成配置</span>
          <el-button type="primary" @click="handleAdd" :disabled="!currentClerk">
            新增配置
          </el-button>
        </div>
      </template>

      <el-alert
        v-if="!currentClerk"
        title="请先选择一个员工"
        type="warning"
        show-icon
        style="margin-bottom: 20px"
      />

      <div v-if="currentClerk">
        <el-descriptions :column="2" border style="margin-bottom: 20px">
          <el-descriptions-item label="员工姓名">{{ currentClerk.clerkName }}</el-descriptions-item>
          <el-descriptions-item label="默认抽成比例">{{ (currentClerk.commissionRate * 100).toFixed(2) }}%</el-descriptions-item>
        </el-descriptions>

        <el-table :data="commissionList" style="width: 100%" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="服务类型" width="150">
            <template #default="{ row }">
              <el-tag>{{ getServiceTypeDisplayName(row.serviceType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="抽成比例" width="150">
            <template #default="{ row }">
              <span style="color: #409eff; font-weight: bold">{{ (row.commissionRate * 100).toFixed(2) }}%</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="commissionForm" :rules="rules" ref="commissionFormRef" label-width="120px">
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="commissionForm.serviceType" placeholder="请选择服务类型" style="width: 100%">
            <el-option
              v-for="type in serviceTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="抽成比例" prop="commissionRate">
          <el-input-number v-model="commissionForm.commissionRate" :min="0" :max="1" :step="0.01" :precision="2" style="width: 100%" />
          <span style="margin-left: 10px">(例如: 0.15 表示 15%)</span>
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
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getClerkServiceCommissionsByClerk,
  createClerkServiceCommission,
  updateClerkServiceCommission,
  deleteClerkServiceCommission
} from '@/api/recharge'
import { getServiceTypes } from '@/api/common'

const props = defineProps({
  clerk: {
    type: Object,
    default: null
  }
})

const commissionList = ref([])
const serviceTypes = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增抽成配置')
const commissionFormRef = ref(null)

const currentClerk = ref(props.clerk)

const commissionForm = reactive({
  id: null,
  clerkId: null,
  serviceType: '',
  commissionRate: 0.1
})

const rules = {
  serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }],
  commissionRate: [{ required: true, message: '请输入抽成比例', trigger: 'blur' }]
}

watch(() => props.clerk, (newClerk) => {
  currentClerk.value = newClerk
  if (newClerk) {
    loadCommissions()
  }
}, { immediate: true })

const getServiceTypeDisplayName = (type) => {
  const typeMap = {
    'WASH_CARE': '洗护',
    'BEAUTY': '美容',
    'PARTIAL_TRIM': '局部修剪',
    'OTHER_SERVICE': '其他服务',
    'FOSTER_CARE': '寄养'
  }
  return typeMap[type] || type
}

const loadCommissions = async () => {
  if (!currentClerk.value) return
  try {
    const data = await getClerkServiceCommissionsByClerk(currentClerk.value.id)
    commissionList.value = data
  } catch (error) {
    ElMessage.error('加载抽成配置失败')
  }
}

const loadServiceTypes = async () => {
  try {
    const data = await getServiceTypes()
    serviceTypes.value = data
  } catch (error) {
    console.error('加载服务类型失败:', error)
    serviceTypes.value = [
      { value: 'WASH_CARE', label: '洗护' },
      { value: 'BEAUTY', label: '美容' },
      { value: 'PARTIAL_TRIM', label: '局部修剪' },
      { value: 'OTHER_SERVICE', label: '其他服务' },
      { value: 'FOSTER_CARE', label: '寄养' }
    ]
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增抽成配置'
  Object.assign(commissionForm, {
    id: null,
    clerkId: currentClerk.value?.id,
    serviceType: '',
    commissionRate: 0.1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑抽成配置'
  Object.assign(commissionForm, {
    id: row.id,
    clerkId: row.clerk?.id || currentClerk.value?.id,
    serviceType: row.serviceType,
    commissionRate: row.commissionRate
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!commissionFormRef.value) return

  await commissionFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (commissionForm.id) {
          await updateClerkServiceCommission(commissionForm.id, commissionForm)
          ElMessage.success('配置更新成功')
        } else {
          await createClerkServiceCommission(commissionForm)
          ElMessage.success('配置创建成功')
        }
        dialogVisible.value = false
        loadCommissions()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该配置吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteClerkServiceCommission(row.id)
    ElMessage.success('配置删除成功')
    loadCommissions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadServiceTypes()
})
</script>

<style scoped>
.clerk-commission-config {
  padding: 10px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
