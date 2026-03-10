
<template>
  <div class="roles">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色权限管理</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="角色列表" name="roles">
          <el-table :data="roleList" style="width: 100%" border>
            <el-table-column prop="name" label="角色名称" width="150" />
            <el-table-column prop="displayName" label="显示名称" width="150" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="权限数量" width="120">
              <template #default="{ row }">
                {{ getPermissionCount(row.name) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="viewRolePermissions(row)">查看权限</el-button>
                <el-button type="success" size="small" @click="editRolePermissions(row)">编辑权限</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="权限列表" name="permissions">
          <el-row :gutter="20">
            <el-col :span="8" v-for="(permissions, category) in groupedPermissions" :key="category">
              <el-card :header="category" class="permission-card">
                <el-checkbox-group v-model="selectedPermissions">
                  <el-checkbox v-for="perm in permissions" :key="perm.name" :label="perm.name" :disabled="true">
                    {{ perm.displayName }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 查看角色权限对话框 -->
    <el-dialog v-model="permissionDialogVisible" title="角色权限详情" width="600px">
      <div v-if="currentRole">
        <h3>{{ currentRole.displayName }}</h3>
        <p>{{ currentRole.description }}</p>
        <el-divider />
        <h4>拥有的权限：</h4>
        <el-tag v-for="perm in getRolePermissions(currentRole.name)" :key="perm" style="margin: 5px;">
          {{ perm }}
        </el-tag>
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 编辑角色权限对话框 -->
    <el-dialog v-model="editDialogVisible" :title="'编辑权限 - ' + (currentEditRole?.displayName || '')" width="800px">
      <el-row :gutter="20">
        <el-col :span="8" v-for="(permissions, category) in groupedPermissions" :key="category">
          <el-card :header="category" class="permission-card">
            <el-checkbox-group v-model="editPermissions">
              <el-checkbox v-for="perm in permissions" :key="perm.name" :label="perm.name">
                {{ perm.displayName }}
              </el-checkbox>
            </el-checkbox-group>
          </el-card>
        </el-col>
      </el-row>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRolePermissions" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRolesAndPermissions, updateRolePermissions } from '@/api/role'

const activeTab = ref('roles')
const roleList = ref([])
const permissionList = ref([])
const rolePermissions = ref({})
const selectedPermissions = ref([])
const permissionDialogVisible = ref(false)
const editDialogVisible = ref(false)
const currentRole = ref(null)
const currentEditRole = ref(null)
const editPermissions = ref([])
const saving = ref(false)

// 按类别分组权限
const groupedPermissions = computed(() => {
  const groups = {}
  permissionList.value.forEach(perm => {
    let category = '其他权限'
    if (perm.name.startsWith('CUSTOMER')) category = '顾客管理'
    else if (perm.name.startsWith('CLERK')) category = '员工管理'
    else if (perm.name.startsWith('TRANSACTION')) category = '交易管理'
    else if (perm.name.startsWith('APPOINTMENT')) category = '预约管理'
    else if (perm.name.startsWith('RECHARGE')) category = '储值管理'
    else if (perm.name.startsWith('REPORT')) category = '报告管理'
    else if (perm.name.startsWith('ROLE')) category = '角色权限'
    else if (perm.name.startsWith('SETTINGS')) category = '系统设置'
    else if (perm.name.startsWith('DASHBOARD')) category = '仪表盘'
    
    if (!groups[category]) {
      groups[category] = []
    }
    groups[category].push(perm)
  })
  return groups
})

const loadData = async () => {
  try {
    const data = await getRolesAndPermissions()
    
    // 转换角色数据
    roleList.value = Object.keys(data.roles).map(key => ({
      name: key,
      displayName: data.roles[key],
      description: getRoleDescription(key)
    }))
    
    // 转换权限数据
    permissionList.value = Object.keys(data.permissions).map(key => ({
      name: key,
      displayName: data.permissions[key]
    }))
    
    rolePermissions.value = data.rolePermissions
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const getRoleDescription = (roleName) => {
  const descriptions = {
    'ADMIN': '拥有系统所有权限，可以管理所有功能和用户',
    'MANAGER': '管理员工和查看报告，监督日常运营',
    'STAFF': '处理日常业务操作，服务客户'
  }
  return descriptions[roleName] || ''
}

const getPermissionCount = (roleName) => {
  const perms = rolePermissions.value[roleName]
  return perms ? perms.length : 0
}

const getRolePermissions = (roleName) => {
  return rolePermissions.value[roleName] || []
}

const viewRolePermissions = (role) => {
  currentRole.value = role
  permissionDialogVisible.value = true
}

const editRolePermissions = (role) => {
  currentEditRole.value = role
  editPermissions.value = [...getRolePermissions(role.name)]
  editDialogVisible.value = true
}

const saveRolePermissions = async () => {
  if (!currentEditRole.value) return
  
  saving.value = true
  try {
    await updateRolePermissions(currentEditRole.value.name, editPermissions.value)
    ElMessage.success('权限保存成功')
    editDialogVisible.value = false
    await loadData() // 重新加载数据
  } catch (error) {
    ElMessage.error('保存权限失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.roles {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.permission-card {
  margin-bottom: 20px;
}
</style>
