
<template>
  <div class="reports">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报表中心</span>
        </div>
      </template>

      <el-row :gutter="20" class="action-buttons">
        <el-col :span="6">
          <el-button type="primary" @click="generateDailyReport" style="width: 100%">生成日报表</el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="success" @click="generateMonthlyReport" style="width: 100%">生成月报表</el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="warning" @click="generateAnnualReport" style="width: 100%">生成年报表</el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="info" @click="generateSalesChart" style="width: 100%">生成销售图表</el-button>
        </el-col>
      </el-row>

      <el-divider />

      <div class="report-preview">
        <el-input
          v-model="reportContent"
          type="textarea"
          :rows="20"
          placeholder="报表内容将显示在这里"
          readonly
        />
      </div>

      <el-divider />

      <el-row :gutter="20">
        <el-col :span="12">
          <el-button type="primary" @click="exportPdf" :disabled="!reportContent" style="width: 100%">导出 PDF</el-button>
        </el-col>
        <el-col :span="12">
          <el-button type="success" @click="exportExcel" :disabled="!reportContent" style="width: 100%">导出 Excel</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getDailyReport, getMonthlyReport, getAnnualReport, exportPdfReport, exportExcelReport, getChartData } from '@/api/report'

const reportContent = ref('')

const generateDailyReport = async () => {
  try {
    const data = await getDailyReport()
    reportContent.value = data
    ElMessage.success('日报表生成成功')
  } catch (error) {
    ElMessage.error('日报表生成失败')
  }
}

const generateMonthlyReport = async () => {
  try {
    const data = await getMonthlyReport()
    reportContent.value = data
    ElMessage.success('月报表生成成功')
  } catch (error) {
    ElMessage.error('月报表生成失败')
  }
}

const generateAnnualReport = async () => {
  try {
    const data = await getAnnualReport()
    reportContent.value = data
    ElMessage.success('年报表生成成功')
  } catch (error) {
    ElMessage.error('年报表生成失败')
  }
}

const generateSalesChart = async () => {
  try {
    await getChartData('monthly')
    ElMessage.success('销售图表生成成功')
  } catch (error) {
    ElMessage.error('销售图表生成失败')
  }
}

const exportPdf = async () => {
  try {
    const blob = await exportPdfReport()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `报表_${Date.now()}.pdf`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('PDF导出成功')
  } catch (error) {
    ElMessage.error('PDF导出失败')
  }
}

const exportExcel = async () => {
  try {
    const blob = await exportExcelReport()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `报表_${Date.now()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('Excel导出成功')
  } catch (error) {
    ElMessage.error('Excel导出失败')
  }
}
</script>

<style scoped>
.reports {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-buttons {
  margin-bottom: 20px;
}

.report-preview {
  margin: 20px 0;
}
</style>
