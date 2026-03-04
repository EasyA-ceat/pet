<template>
  <div class="recharge-management">
    <el-card class="header-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="充值活动" name="activities">
          <RechargeActivities />
        </el-tab-pane>
        <el-tab-pane label="充值记录" name="records">
          <RechargeRecords />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import RechargeActivities from '@/components/RechargeActivities.vue'
import RechargeRecords from '@/components/RechargeRecords.vue'

const activeTab = ref('activities')
</script>

<style scoped>
.recharge-management {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}
</style>
