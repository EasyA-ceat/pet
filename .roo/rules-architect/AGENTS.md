
# Project Architecture Rules (Non-Obvious Only)

## 系统架构
- **前端**：JavaFX 构建的桌面应用界面
- **后端**：Spring Boot 提供的 RESTful 服务和数据持久化
- **数据库**：SQLite 轻量级数据库

## 架构约束
- 所有 JavaFX 控制器必须是 Spring 管理的 bean
- 数据访问必须通过 JPA 仓库进行
- 业务逻辑必须封装在 Spring 服务中

## 数据库设计
- 使用 JPA 注解定义数据模型
- SQLite 数据库文件自动创建和更新
- 数据库文件存储在用户主目录下的隐藏文件夹中

