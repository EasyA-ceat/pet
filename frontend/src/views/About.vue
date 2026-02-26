
<template>
  <div class="about">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>关于</span>
        </div>
      </template>

      <div class="about-content">
        <el-result icon="success" title="宠物管理系统">
          <template #sub-title>
            版本: 1.0.2
          </template>
          <template #extra>
            <div class="info-section">
              <h3>系统简介</h3>
              <p>这是一个基于 Vue 3 + Spring Boot 的宠物管理系统，用于管理宠物店铺的顾客、员工、交易和报表等业务。</p>
              
              <h3>技术栈</h3>
              <ul>
                <li>前端框架: Vue 3</li>
                <li>UI组件库: Element Plus</li>
                <li>后端框架: Spring Boot</li>
                <li>数据库: SQLite</li>
                <li>构建工具: Vite / Maven</li>
              </ul>
              
              <h3>功能特性</h3>
              <ul>
                <li>仪表盘: 统计数据展示</li>
                <li>顾客管理: 顾客信息和照片管理</li>
                <li>财务管理: 交易记录和提成计算</li>
                <li>员工管理: 员工信息和提成率设置</li>
                <li>报表中心: 日报、月报、年报生成</li>
                <li>系统设置: 系统配置管理</li>
              </ul>
              
              <h3>版权信息</h3>
              <p>© 2024 Pet Management System. All rights reserved.</p>
            </div>
          </template>
        </el-result>
      </div>
    </el-card>
  </div>
</template>

<script setup>
// About page - simple informational page
</script>

<style scoped>
.about {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.about-content {
  text-align: center;
}

.info-section {
  text-align: left;
  max-width: 600px;
  margin: 0 auto;
}

.info-section h3 {
  margin: 20px 0 10px;
  color: #333;
}

.info-section p {
  margin: 10px 0;
  color: #666;
  line-height: 1.6;
}

.info-section ul {
  list-style: none;
  padding: 0;
}

.info-section li {
  margin: 8px 0;
  color: #666;
  padding-left: 20px;
  position: relative;
}

.info-section li::before {
  content: "•";
  position: absolute;
  left: 0;
  color: #409EFF;
}
</style>
