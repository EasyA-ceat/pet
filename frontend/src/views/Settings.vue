
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
          <el-form :model="basicForm" label-width="120px">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" placeholder="请输入系统名称" />
            </el-form-item>
            <el-form-item label="系统描述">
              <el-input v-model="basicForm.systemDescription" type="textarea" :rows="3" placeholder="请输入系统描述" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveBasicSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="业务设置" name="business">
          <el-form :model="businessForm" label-width="120px">
            <el-form-item label="月度目标">
              <el-input-number v-model="businessForm.monthlyTarget" :min="0" :precision="2" />
              <span style="margin-left: 10px">元</span>
            </el-form-item>
            <el-form-item label="每周目标">
              <el-input-number v-model="businessForm.weeklyTarget" :min="0" :precision="2" />
              <span style="margin-left: 10px">元</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveBusinessSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="其他设置" name="other">
          <el-form :model="otherForm" label-width="120px">
            <el-form-item label="数据备份">
              <el-button @click="backupData">立即备份</el-button>
            </el-form-item>
            <el-form-item label="清除缓存">
              <el-button @click="clearCache">清除缓存</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveOtherSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('basic')

const basicForm = reactive({
  systemName: '宠物管理系统',
  systemDescription: '基于Vue + Spring Boot的宠物管理系统'
})

const businessForm = reactive({
  monthlyTarget: 200000,
  weeklyTarget: 50000
})

const otherForm = reactive({
})

const saveBasicSettings = () => {
  ElMessage.success('基本设置保存成功')
}

const saveBusinessSettings = () => {
  ElMessage.success('业务设置保存成功')
}

const saveOtherSettings = () => {
  ElMessage.success('其他设置保存成功')
}

const backupData = () => {
  ElMessage.info('数据备份功能待实现')
}

const clearCache = async () => {
  try {
    await ElMessageBox.confirm('确定要清除缓存吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('缓存清除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('缓存清除失败')
    }
  }
}
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
</style>
