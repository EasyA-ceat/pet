
# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## 架构重要变更
- ⚠️ **重要**：JavaFX前端已被废弃，现已完全迁移到Vue 3 + Vite前端
- 前端代码位于 `frontend/` 目录下
- 后端通过REST API与前端通信，API控制器位于 `src/main/java/com/pet/management/controller/` 目录

## 构建/测试命令
- 运行单个测试：`mvn -Dtest=TestName test`
- 打包EXE：`mvn clean package` 会同时生成JAR和EXE
- 前端开发：进入 `frontend/` 目录，运行 `npm run dev`

## 关键发现
- EXE文件必须使用 `org.springframework.boot.loader.JarLauncher` 作为主类，而不是应用程序主类
- Maven仓库配置了阿里云和清华镜像源
- 有一个独立的 `frontend/` 目录包含Vue前端代码
- 后端提供完整的REST API接口，支持前端的所有功能

## 数据库
- SQLite数据库文件位于 `user.home/.pet-management-system/pet_db.sqlite`
- 使用 `hibernate-community-dialects` 提供SQLite方言支持

## 应用程序入口
- Spring Boot入口：`PetManagementSystem.java`
- API控制器：`ApiCustomerController`、`ApiTransactionController`、`ApiClerkController`、`ApiDashboardController`、`ApiReportController`
- Vue前端入口：`frontend/src/main.js`

## 遗留代码说明
- `src/main/java/com/pet/management/view/` 目录下的JavaFX控制器和FXML文件为遗留代码，不再使用
- 新功能开发应在Vue前端和REST API控制器中进行

## 计划文件保存
- 计划文件默认保存到 `C:\Users\27760\Desktop\pet\.claude\` 目录下
