
# Project Coding Rules (Non-Obvious Only)

## 架构约束
- JavaFX 控制器必须由 Spring 容器管理，使用 `FXMLLoader.setControllerFactory(context::getBean)` 方法
- 所有数据模型类必须位于 `src/main/java/com/pet/management/model/` 目录下
- 数据库操作接口必须继承 JpaRepository 并位于 `src/main/java/com/pet/management/repository/` 目录下

## 资源管理
- 图片资源必须放在 `src/main/resources/images/` 目录下
- FXML 文件必须放在 `src/main/resources/fxml/` 目录下
- CSS 文件必须放在 `src/main/resources/css/` 目录下

## 图片处理
- 使用 Thumbnailator 库处理图片缩略图（`net.coobird.thumbnailator`）
- 使用 metadata-extractor 库提取图片元数据（`com.drewnoakes.metadata-extractor`）

## 图表生成
- 使用 XChart 库生成图表（`org.knowm.xchart`）

## PDF 导出
- 使用 iTextPDF 库导出 PDF 报告（`com.itextpdf`）

## 数据库
- SQLite 数据库文件自动创建在 `user.home/.pet-management-system/pet_db.sqlite` 目录下
- 使用 SQLite JDBC 驱动和 Hibernate SQLite 方言

