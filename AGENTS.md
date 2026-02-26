
# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## 构建/测试命令
- 运行单个测试：`mvn -Dtest=TestName test`
- 打包EXE：`mvn clean package` 会同时生成JAR和EXE

## 关键发现
- EXE文件必须使用 `org.springframework.boot.loader.JarLauncher` 作为主类，而不是应用程序主类
- Maven仓库配置了阿里云和清华镜像源
- 有一个独立的 `frontend/` 目录包含Vue前端代码

## 数据库
- SQLite数据库文件位于 `user.home/.pet-management-system/pet_db.sqlite`
- 使用 `hibernate-community-dialects` 提供SQLite方言支持

## 应用程序入口
- JavaFX入口：`MainApplication.java`
- Spring Boot入口：`PetManagementSystem.java`
- JavaFX控制器必须由Spring容器管理，使用 `FXMLLoader.setControllerFactory(context::getBean)`

