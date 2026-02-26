
# Project Debug Rules (Non-Obvious Only)

## 日志位置
- 应用程序日志文件存储在 `user.home/.pet-management-system/logs/pet-management.log`

## 常见问题
- **图标加载失败**：检查 `src/main/resources/images/icon.png` 是否存在
- **FXML 加载失败**：检查 FXML 文件路径是否正确，确保使用 `/fxml/` 前缀
- **数据库连接失败**：确保用户主目录下的 `.pet-management-system` 文件夹有读写权限

## 运行模式
- 使用 `mvn spring-boot:run` 运行应用程序（包含 Spring Boot 支持）
- 使用 `mvn javafx:run` 直接运行 JavaFX 应用（不包含 Spring Boot 支持）

