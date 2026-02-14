# Project Documentation Rules (Non-Obvious Only)

## 项目结构
- `src/main/java/com/pet/management/MainApplication.java`：JavaFX 应用入口点
- `src/main/java/com/pet/management/PetManagementSystem.java`：Spring Boot 主类
- `src/main/resources/application.properties`：应用程序配置文件
- `src/main/resources/fxml/`：JavaFX 界面布局文件
- `src/main/resources/css/`：样式文件
- `src/main/resources/images/`：图片资源

## 配置说明
- 数据库配置：`spring.datasource.url` 指向 SQLite 数据库文件
- JPA 配置：`spring.jpa.hibernate.ddl-auto=update` 自动更新数据库结构
- 文件上传配置：最大文件大小为 10MB（`spring.servlet.multipart.max-file-size=10MB`）

## 依赖库
- JavaFX：用于构建桌面应用界面
- Spring Boot：提供后端服务支持
- SQLite：轻量级数据库
- Thumbnailator：图片处理
- metadata-extractor：图片元数据提取
- XChart：图表生成
- iTextPDF：PDF 导出

## 注意事项
- 应用程序需要 Java 17 或更高版本
- 数据库文件会自动创建在用户主目录下的 `.pet-management-system` 文件夹中
