# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## 项目概述

这是一个基于 Spring Boot + JavaFX 的宠物管理系统桌面应用，使用 SQLite 数据库存储数据。

## 构建/测试命令

- 编译项目：`mvn clean compile`
- 运行应用：`mvn spring-boot:run` 或 `mvn javafx:run`
- 打包应用：`mvn clean package`
- 运行测试：`mvn test`

## 代码风格

- 遵循 Spring Boot 和 JavaFX 最佳实践
- 使用 4 空格缩进
- 类名使用 PascalCase，方法名和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE

## 项目特定模式

- **数据库配置**：SQLite 数据库文件存储在 `user.home/.pet-management-system/pet_db.sqlite`
- **资源加载**：FXML、CSS 和图片资源从 `src/main/resources` 目录加载
- **控制器管理**：JavaFX 控制器由 Spring 容器管理，使用 `FXMLLoader.setControllerFactory(context::getBean)` 方法
- **图片处理**：使用 Thumbnailator 库处理图片缩略图，使用 metadata-extractor 库提取图片元数据
- **图表生成**：使用 XChart 库生成图表
- **PDF 导出**：使用 iTextPDF 库导出 PDF 报告

## 关键文件和目录

- `src/main/java/com/pet/management/MainApplication.java`：应用程序入口点
- `src/main/java/com/pet/management/PetManagementSystem.java`：Spring Boot 主类
- `src/main/java/com/pet/management/model/`：数据模型类
- `src/main/java/com/pet/management/repository/`：数据库操作接口
- `src/main/java/com/pet/management/service/`：业务逻辑层
- `src/main/java/com/pet/management/view/controller/`：JavaFX 控制器
- `src/main/resources/fxml/`：FXML 界面文件
- `src/main/resources/css/`：CSS 样式文件
- `src/main/resources/images/`：图片资源
- `src/main/resources/application.properties`：应用程序配置文件

## 注意事项

- 应用程序需要 Java 17 或更高版本
- 数据库文件会自动创建在用户主目录下的 .pet-management-system 文件夹中
- 图片资源必须放在 src/main/resources/images/ 目录下
- FXML 文件必须放在 src/main/resources/fxml/ 目录下
- CSS 文件必须放在 src/main/resources/css/ 目录下

## 常见问题与解决方案

### EXE 文件无法启动（找不到主类）

**问题现象**：运行 EXE 文件时提示 "错误: 找不到或无法加载主类 com.pet.management.MainApplication"

**原因**：项目使用了 Spring Boot 的 repackage 插件，JAR 文件被重新打包后，真正的启动类不是应用程序的主类，而是 Spring Boot 的启动器类。

**解决方案**：在 pom.xml 的 launch4j 配置中，将 mainClass 设置为 `org.springframework.boot.loader.JarLauncher` 而不是 `com.pet.management.MainApplication`。

正确的配置示例：
```xml
<classPath>
    <mainClass>org.springframework.boot.loader.JarLauncher</mainClass>
</classPath>
```

**验证方法**：可以通过 `jar xf target/xxx.jar META-INF/MANIFEST.MF` 命令解压查看 MANIFEST.MF 文件，确认 Main-Class 的值。
