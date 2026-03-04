<template>
  <div class="recharge-activities">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增活动</el-button>
    </div>

    <el-table :data="activityList" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="活动名称" width="200" />
      <el-table-column label="最低充值金额" width="150">
        <template #default="{ row }">
          ¥{{ row.minAmount?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="赠送金额" width="150">
        <template #default="{ row }">
          ¥{{ row.bonusAmount?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始日期" width="180">
        <template #default="{ row }">
          {{ formatDate(row.startDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="endDate" label="结束日期" width="180">
        <template #default="{ row }">
          {{ formatDate(row.endDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="isActive" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isActive ? 'success' : 'danger'">
            {{ row.isActive ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="activityForm" :rules="rules" ref="activityFormRef" label-width="120px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="activityForm.name" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="最低充值金额" prop="minAmount">
          <el-input-number v-model="activityForm.minAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="赠送金额" prop="bonusAmount">
          <el-input-number v-model="activityForm.bonusAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="activityForm.startDate" type="date" placeholder="选择开始日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="activityForm.endDate" type="date" placeholder="选择结束日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isActive">
          <el-switch v-model="activityForm.isActive" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getRechargeActivityList,
  createRechargeActivity,
  updateRechargeActivity,
  deleteRechargeActivity
} from '@/api/recharge'

const activityList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增活动')
const activityFormRef = ref(null)

const activityForm = reactive({
  id: null,
  name: '',
  minAmount: 0,
  bonusAmount: 0,
  startDate: null,
  endDate: null,
  isActive: true
})

const rules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  minAmount: [{ required: true, message: '请输入最低充值金额', trigger: 'blur' }],
  bonusAmount: [{ required: true, message: '请输入赠送金额', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const loadActivities = async () => {
  try {
    const data = await getRechargeActivityList()
    activityList.value = data
  } catch (error) {
    ElMessage.error('加载活动数据失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增活动'
  Object.assign(activityForm, {
    id: null,
    name: '',
    minAmount: 0,
    bonusAmount: 0,
    startDate: null,
    endDate: null,
    isActive: true
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  Object.assign(activityForm, {
    ...row,
    startDate: row.startDate ? new Date(row.startDate) : null,
    endDate: row.endDate ? new Date(row.endDate) : null
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!activityFormRef.value) return

  await activityFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (activityForm.id) {
          await updateRechargeActivity(activityForm.id, activityForm)
          ElMessage.success('活动更新成功')
        } else {
          await createRechargeActivity(activityForm)
          ElMessage.success('活动创建成功')
        }
        dialogVisible.value = false
        loadActivities()
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '操作失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteRechargeActivity(row.id)
    ElMessage.success('活动删除成功')
    loadActivities()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadActivities()
})
</script>

<style scoped>
.recharge-activities {
  padding: 10px 0;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
