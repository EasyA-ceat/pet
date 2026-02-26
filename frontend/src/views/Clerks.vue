
<template>
  <div class="clerks">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工管理</span>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item>
          <el-input v-model="searchForm.keyword" placeholder="搜索员工姓名或电话" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleRefresh">刷新</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="clerkList" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clerkName" label="员工姓名" width="150" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="commissionRate" label="提成比例" width="120">
          <template #default="{ row }">
            {{ (row.commissionRate * 100).toFixed(2) }}%
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
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="clerkForm" :rules="rules" ref="clerkFormRef" label-width="100px">
        <el-form-item label="员工姓名" prop="clerkName">
          <el-input v-model="clerkForm.clerkName" placeholder="请输入员工姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="clerkForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="提成比例" prop="commissionRate">
          <el-input-number v-model="clerkForm.commissionRate" :min="0" :max="1" :step="0.01" :precision="2" />
          <span style="margin-left: 10px">(例如: 0.1 表示 10%)</span>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="clerkForm.notes" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { getClerkList, createClerk, updateClerk, deleteClerk } from '@/api/clerk'

const clerkList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加员工')
const clerkFormRef = ref(null)

const searchForm = reactive({
  keyword: ''
})

const clerkForm = reactive({
  id: null,
  clerkName: '',
  phone: '',
  commissionRate: 0.1,
  notes: ''
})

const rules = {
  clerkName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  commissionRate: [{ required: true, message: '请输入提成比例', trigger: 'blur' }]
}

const loadClerkData = async () => {
  try {
    const data = await getClerkList()
    clerkList.value = data
  } catch (error) {
    ElMessage.error('加载员工数据失败')
  }
}

const handleSearch = () => {
  ElMessage.info('搜索功能待实现')
}

const handleRefresh = () => {
  loadClerkData()
}

const handleAdd = () => {
  dialogTitle.value = '添加员工'
  Object.assign(clerkForm, {
    id: null,
    clerkName: '',
    phone: '',
    commissionRate: 0.1,
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  Object.assign(clerkForm, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!clerkFormRef.value) return
  
  await clerkFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (clerkForm.id) {
          await updateClerk(clerkForm.id, clerkForm)
          ElMessage.success('员工更新成功')
        } else {
          await createClerk(clerkForm)
          ElMessage.success('员工添加成功')
        }
        dialogVisible.value = false
        loadClerkData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该员工吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteClerk(row.id)
    ElMessage.success('员工删除成功')
    loadClerkData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadClerkData()
})
</script>

<style scoped>
.clerks {
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
</style>
