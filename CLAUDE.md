# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述
宠物管理系统是基于 **Spring Boot 3 + JavaFX 17** 的桌面应用，同时内置 **Vue 3 + Element Plus** 前端管理界面，使用 SQLite 作为嵌入式数据库。

## 技术栈
- **后端**: Java 17, Spring Boot 3.1, Spring Security, JWT, Spring Data JPA
- **桌面UI**: JavaFX 17, JFoenix, ControlsFX
- **前端**: Vue 3, Element Plus, Pinia, Vue Router, Vite
- **数据库**: SQLite 3.x
- **构建工具**: Maven 3.x

## 常用命令

### 后端开发
```bash
# 构建项目并打包（生成jar和exe文件）
mvn clean package

# 运行应用
mvn spring-boot:run

# 运行单元测试
mvn test
```

### 前端开发
```bash
# 进入前端目录
cd frontend

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

## 代码架构

### 后端结构
```
com.pet.management/
├── PetManagementSystem.java    # Spring Boot 启动类
├── MainApplication.java        # JavaFX 启动类
├── config/                     # 配置类（安全、JWT、Web等）
├── controller/                 # API 控制器
├── entity/                     # 实体类（JPA 映射）
├── repository/                 # JPA 仓库接口
├── service/                    # 业务逻辑层
├── utils/                      # 工具类
└── fxml/                       # JavaFX 界面文件
```

### 前端结构
```
frontend/
├── src/
│   ├── views/                  # 页面组件
│   ├── router/                 # 路由配置
│   ├── store/                  # Pinia 状态管理
│   ├── api/                    # API 接口封装
│   ├── utils/                  # 工具函数
│   └── App.vue                 # 根组件
└── vite.config.js              # Vite 配置
```

## 核心模块
- 顾客管理
- 宠物管理
- 财务管理
- 员工管理
- 报表统计
- 系统设置
- 角色权限管理

## 重要信息
- 数据库文件位置: `用户主目录/.pet-management-system/pet_db.sqlite`
- 后端默认端口: 8080
- 前端开发端口: 5173
- 打包产物输出目录: `target/`
- 主入口类: `com.pet.management.MainApplication`
