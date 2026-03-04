
<template>
  <div class="clerks">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工管理</span>
          <el-button type="primary" @click="handleAdd">新增员工</el-button>
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
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.role)">{{ getRoleDisplayName(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="commissionRate" label="提成比例" width="120">
          <template #default="{ row }">
            {{ (row.commissionRate * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" size="small" @click="handleCommissionConfig(row)">抽成配置</el-button>
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
        <el-form-item label="用户名" prop="username" v-if="!clerkForm.id">
          <el-input v-model="clerkForm.username" placeholder="请输入登录用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!clerkForm.id">
          <el-input v-model="clerkForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="clerkForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="经理" value="MANAGER" />
            <el-option label="员工" value="STAFF" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="clerkForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="提成比例" prop="commissionRate">
          <el-input-number v-model="clerkForm.commissionRate" :min="0" :max="1" :step="0.01" :precision="2" />
          <span style="margin-left: 10px">(例如: 0.1 表示 10%)</span>
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-switch v-model="clerkForm.enabled" active-text="启用" inactive-text="禁用" />
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

    <!-- 抽成配置对话框 -->
    <el-dialog v-model="commissionDialogVisible" title="服务抽成配置" width="800px">
      <ClerkCommissionConfig :clerk="selectedClerk" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getClerkList, createClerk, updateClerk, deleteClerk } from '@/api/clerk'
import ClerkCommissionConfig from '@/components/ClerkCommissionConfig.vue'

const clerkList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加员工')
const clerkFormRef = ref(null)
const commissionDialogVisible = ref(false)
const selectedClerk = ref(null)

const searchForm = reactive({
  keyword: ''
})

const clerkForm = reactive({
  id: null,
  clerkName: '',
  username: '',
  password: '',
  role: 'STAFF',
  phone: '',
  commissionRate: 0.1,
  enabled: true,
  notes: ''
})

const rules = {
  clerkName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  commissionRate: [{ required: true, message: '请输入提成比例', trigger: 'blur' }]
}

const getRoleDisplayName = (role) => {
  const roleMap = {
    'ADMIN': '管理员',
    'MANAGER': '经理',
    'STAFF': '员工'
  }
  return roleMap[role] || role
}

const getRoleTagType = (role) => {
  const typeMap = {
    'ADMIN': 'danger',
    'MANAGER': 'warning',
    'STAFF': 'info'
  }
  return typeMap[role] || 'info'
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
    username: '',
    password: '',
    role: 'STAFF',
    phone: '',
    commissionRate: 0.1,
    enabled: true,
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

const handleCommissionConfig = (row) => {
  selectedClerk.value = row
  commissionDialogVisible.value = true
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

