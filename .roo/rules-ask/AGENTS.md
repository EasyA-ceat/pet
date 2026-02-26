
# Project Documentation Rules (Non-Obvious Only)

## 项目结构
- `src/main/java/com/pet/management/MainApplication.java`：JavaFX 应用入口点
- `src/main/java/com/pet/management/PetManagementSystem.java`：Spring Boot 主类
- 有一个独立的 `frontend/` 目录包含 Vue 前端代码（不用于主应用）

## 配置说明
- 数据库配置：`spring.datasource.url` 指向 SQLite 数据库文件
- JPA 配置：`spring.jpa.hibernate.ddl-auto=update` 自动更新数据库结构

## 注意事项
- 数据库文件会自动创建在用户主目录下的 `.pet-management-system` 文件夹中

