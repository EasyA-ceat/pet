# 系统设置模块完善计划

## 上下文
当前系统设置模块存在以下问题需要完善：
1. 前端 `Settings.vue` 的数据备份/恢复/清除功能为空实现，仅显示"开发中"提示
2. 缺少系统设置相关的RESTful API接口
3. 架构设计不规范：功能直接在控制器层实现，缺少Service和Repository层抽象
4. 自动备份定时任务逻辑未实现
5. 缺少配置验证逻辑（如路径有效性、权限检查等）

## 实现方案
### 阶段1：完善后端架构
1. 创建系统设置相关的分层架构：
   - `SystemSettings.java` 实体类（存储配置信息）
   - `SystemSettingsRepository.java` JPA仓库接口
   - `SystemSettingsService.java` 业务逻辑层
   - `ApiSystemSettingsController.java` REST API控制器

2. 实现自动备份定时任务：
   - 使用Spring Scheduler实现定时自动备份功能
   - 支持用户配置备份频率和保留天数

3. 添加配置验证逻辑：
   - 路径有效性检查
   - 文件权限验证
   - 配置参数合法性校验

### 阶段2：完善前端功能
1. 实现 `Settings.vue` 中的三个功能方法：
   - `backupData()` 对接后端备份API
   - `restoreData()` 对接后端恢复API
   - `clearData()` 对接后端清除API

2. 完善前端表单验证：
   - 路径格式验证
   - 操作二次确认
   - 加载状态提示

## 要修改/创建的文件
### 后端
- `src/main/java/com/pet/management/entity/SystemSettings.java` (新建)
- `src/main/java/com/pet/management/repository/SystemSettingsRepository.java` (新建)
- `src/main/java/com/pet/management/service/SystemSettingsService.java` (新建)
- `src/main/java/com/pet/management/controller/ApiSystemSettingsController.java` (新建)
- `src/main/java/com/pet/management/config/SchedulerConfig.java` (新建，定时任务配置)
- `src/main/java/com/pet/management/view/controller/SettingsController.java` (重构，调用Service层)

### 前端
- `frontend/src/views/Settings.vue` (修改，实现功能方法)
- `frontend/src/api/settings.js` (新建，API接口封装)

## 验证步骤
1. 运行后端服务，测试API接口：
   - GET /api/settings 获取配置
   - POST /api/settings 保存配置
   - POST /api/settings/backup 手动备份
   - POST /api/settings/restore 恢复数据
   - POST /api/settings/clear 清除数据
2. 前端页面测试：
   - 基本设置保存/重置功能
   - 数据库设置保存/重置功能
   - 数据备份/恢复/清除功能
   - 自动备份开关功能
3. 定时任务测试：验证自动备份按配置时间执行
4. 异常场景测试：
   - 路径不存在时的错误提示
   - 权限不足时的错误提示
   - 备份文件损坏时的恢复失败处理
