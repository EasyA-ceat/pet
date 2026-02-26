
# 启动指南

## 项目概述

已成功完成从 JavaFX 到 Vue 前端的转换！现在您有两个版本可以使用：
- 原有 JavaFX 桌面应用
- 新的 Vue Web 应用

## 启动步骤

### 1. 启动 Spring Boot 后端

#### 方式一：使用 Maven（推荐）
```bash
# 在项目根目录运行
mvn spring-boot:run
```

#### 方式二：使用 IDE
直接运行 `PetManagementSystem.java` 或 `MainApplication.java`

后端将在 `http://localhost:8080` 启动

### 2. 启动 Vue 前端

#### 第一步：安装依赖
```bash
cd frontend
npm install
```

#### 第二步：启动开发服务器
```bash
npm run dev
```

前端将在 `http://localhost:3000` 启动

### 3. 访问应用

打开浏览器访问：`http://localhost:3000`

## 已完成的功能

### 前端（Vue）
- ✅ 完整的项目配置
- ✅ API 接口层封装
- ✅ 主布局组件（侧边栏导航）
- ✅ 仪表盘页面
- ✅ 顾客管理页面
- ✅ 财务管理页面
- ✅ 员工管理页面
- ✅ 报表中心页面
- ✅ 系统设置页面
- ✅ 关于页面

### 后端（Spring Boot）
- ✅ 仪表盘 API 控制器
- ✅ 顾客管理 API 控制器
- ✅ 交易管理 API 控制器
- ✅ 员工管理 API 控制器
- ✅ 报表中心 API 控制器
- ✅ CORS 跨域配置
- ✅ 固定端口配置（8080）

## 技术栈

### 前端
- Vue 3
- Vue Router
- Pinia
- Element Plus
- Axios
- Vite

### 后端
- Spring Boot 3.1.0
- Spring Data JPA
- SQLite
- Hibernate

## 项目结构

```
pet/
├── frontend/              # Vue 前端
│   ├── src/
│   │   ├── api/          # API 接口
│   │   ├── components/   # 组件
│   │   ├── views/        # 页面
│   │   ├── router/       # 路由
│   │   ├── utils/        # 工具
│   │   └── main.js
│   └── package.json
├── src/main/java/com/pet/management/
│   ├── controller/       # API 控制器
│   │   ├── ApiDashboardController.java
│   │   ├── ApiCustomerController.java
│   │   ├── ApiTransactionController.java
│   │   ├── ApiClerkController.java
│   │   └── ApiReportController.java
│   ├── config/          # 配置
│   │   └── CorsConfig.java
│   └── ...
└── plans/               # 项目文档
    ├── javafx-to-vue-migration-plan.md
    └── startup-guide.md
```

## 注意事项

1. 数据库文件会自动创建在：`~/.pet-management-system/pet_db.sqlite`
2. 原有 JavaFX 应用仍然可以正常使用
3. Vue 前端通过 API 代理访问后端（配置在 vite.config.js）
4. 两个版本共享同一个数据库

## 下一步

- 完善图片上传功能
- 添加用户认证
- 优化图表展示
- 添加更多报表功能
- 测试和优化

## 常见问题

### Q: 端口被占用怎么办？
A: 修改 `application.properties` 中的 `server.port` 和 `vite.config.js` 中的代理配置

### Q: 数据库在哪里？
A: 在用户主目录下的 `.pet-management-system` 文件夹中

### Q: 可以同时运行两个版本吗？
A: 可以，但建议只运行一个后端实例

