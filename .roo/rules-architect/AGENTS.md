# Project Architecture Rules (Non-Obvious Only)

## 系统架构
- **前端**：JavaFX 构建的桌面应用界面
- **后端**：Spring Boot 提供的 RESTful 服务和数据持久化
- **数据库**：SQLite 轻量级数据库

## 核心组件
- **JavaFX 控制器**：处理用户交互和界面逻辑
- **Spring 服务**：处理业务逻辑
- **JPA 仓库**：处理数据库操作
- **数据模型**：表示业务实体

## 架构约束
- 所有 JavaFX 控制器必须是 Spring 管理的 bean
- 数据访问必须通过 JPA 仓库进行
- 业务逻辑必须封装在 Spring 服务中

## 资源加载
- 使用 Spring 的资源加载机制加载外部资源
- 所有资源文件必须放在 `src/main/resources` 目录下

## 数据库设计
- 使用 JPA 注解定义数据模型
- SQLite 数据库文件自动创建和更新
- 数据库文件存储在用户主目录下的隐藏文件夹中

## 性能优化
- 使用 Thumbnailator 生成缩略图以优化图片显示
- 数据库查询使用 JPA 查询方法进行优化
