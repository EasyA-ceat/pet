<template>
  <div class="settings">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本设置" name="basic">
          <el-form :model="basicForm" label-width="150px">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" placeholder="请输入系统名称" />
            </el-form-item>
            <el-form-item label="系统描述">
              <el-input v-model="basicForm.systemDescription" type="textarea" :rows="3" placeholder="请输入系统描述" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveBasicSettings">保存设置</el-button>
              <el-button @click="resetBasicSettings">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="数据库设置" name="database">
          <el-form :model="databaseForm" label-width="150px">
            <el-form-item label="数据库路径">
              <el-input v-model="databaseForm.databasePath" placeholder="数据库文件路径" />
            </el-form-item>
            <el-form-item label="备份路径">
              <el-input v-model="databaseForm.backupPath" placeholder="备份文件存储路径" />
            </el-form-item>
            <el-form-item label="自动备份">
              <el-switch v-model="databaseForm.autoBackup" />
              <span style="margin-left: 10px">启用自动备份</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveDatabaseSettings">保存设置</el-button>
              <el-button @click="resetDatabaseSettings">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="数据管理" name="data">
          <el-form label-width="150px">
            <el-form-item label="数据备份">
              <el-button type="primary" @click="backupData">立即备份</el-button>
              <span style="margin-left: 10px; color: #999">备份当前数据库到指定路径</span>
            </el-form-item>
            <el-form-item label="数据恢复">
              <el-button type="warning" @click="restoreData">恢复数据</el-button>
              <span style="margin-left: 10px; color: #999">从备份文件恢复数据库</span>
            </el-form-item>
            <el-form-item label="清除数据">
              <el-button type="danger" @click="clearData">清除数据</el-button>
              <span style="margin-left: 10px; color: #999">此操作不可恢复，请谨慎操作！</span>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="关于" name="about">
          <div class="about-info">
            <el-descriptions title="系统信息" :column="1" border>
              <el-descriptions-item label="系统名称">{{ basicForm.systemName }}</el-descriptions-item>
              <el-descriptions-item label="版本号">1.0.0</el-descriptions-item>
              <el-descriptions-item label="开发者">宠物管理系统开发团队</el-descriptions-item>
              <el-descriptions-item label="联系方式">support@petmanagementsystem.com</el-descriptions-item>
              <el-descriptions-item label="版权信息">© 2026 宠物管理系统. All rights reserved.</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('basic')

const basicForm = reactive({
  systemName: '宠物管理系统',
  systemDescription: '基于Vue + Spring Boot的宠物管理系统'
})

const databaseForm = reactive({
  databasePath: '',
  backupPath: '',
  autoBackup: false
})

const saveBasicSettings = () => {
  ElMessage.success('基本设置保存成功')
}

const resetBasicSettings = () => {
  basicForm.systemName = '宠物管理系统'
  basicForm.systemDescription = '基于Vue + Spring Boot的宠物管理系统'
  ElMessage.info('基本设置已重置')
}

const saveDatabaseSettings = () => {
  ElMessage.success('数据库设置保存成功')
}

const resetDatabaseSettings = () => {
  databaseForm.databasePath = ''
  databaseForm.backupPath = ''
  databaseForm.autoBackup = false
  ElMessage.info('数据库设置已重置')
}

const backupData = async () => {
  try {
    ElMessage.info('数据备份功能开发中...')
  } catch (error) {
    ElMessage.error('数据备份失败')
  }
}

const restoreData = async () => {
  try {
    await ElMessageBox.confirm('确定要恢复数据吗？当前数据将被覆盖！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.info('数据恢复功能开发中...')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('数据恢复失败')
    }
  }
}

const clearData = async () => {
  try {
    await ElMessageBox.confirm('确定要清除所有数据吗？此操作不可恢复！', '危险操作', {
      confirmButtonText: '确定清除',
      cancelButtonText: '取消',
      type: 'error'
    })
    ElMessage.info('数据清除功能开发中...')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('数据清除失败')
    }
  }
}

onMounted(() => {
  // 加载设置
})
</script>

<style scoped>
.settings {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.about-info {
  padding: 20px 0;
}
</style>
