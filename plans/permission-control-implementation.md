
# 权限控制系统实现说明

## 概述
本文档说明了宠物管理系统的权限控制系统的实现方案，包括后端API权限控制和前端界面权限控制。

## 权限模型

### 角色定义
系统定义了三种角色：

1. **ADMIN（管理员）**
   - 拥有系统所有权限
   - 可以管理员工、设置系统参数等

2. **MANAGER（经理）**
   - 可以查看和管理日常业务
   - 可以管理员工信息
   - 可以查看和生成报告
   - 但不能进行系统设置

3. **STAFF（员工）**
   - 只能处理日常业务操作
   - 可以查看和管理顾客、预约、交易
   - 不能访问员工管理和系统设置

### 权限模块
系统将功能划分为以下权限模块：
- `DASHBOARD` - 仪表盘
- `CUSTOMER` - 顾客管理
- `APPOINTMENT` - 预约管理
- `TRANSACTION` - 财务管理
- `CLERK` - 员工管理
- `REPORT` - 报表中心
- `SETTINGS` - 系统设置

## 后端实现

### 数据模型扩展
1. **Role.java** - 角色枚举
2. **Permission.java** - 权限枚举
3. **Clerk.java** - 员工模型添加了role字段

### 安全配置更新
1. **ClerkDetailsService.java** - 加载用户时包含角色信息
2. **ApiClerkController.java** - 添加了基于角色的访问控制注解
3. **ApiRoleController.java** - 新增角色权限管理API
4. **AuthController.java** - 登录时返回用户角色信息
5. **AdminDataInitializer.java** - 初始化管理员角色

### API权限注解
使用Spring Security的`@PreAuthorize`注解进行API级别的权限控制：
- `@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")` - 管理员和经理可访问
- `@PreAuthorize("hasRole('ADMIN')")` - 仅管理员可访问

## 前端实现

### 权限管理API
- **role.js** - 新增角色权限API调用模块

### 界面更新
1. **Login.vue** - 登录时保存用户角色信息
2. **Layout.vue** - 根据用户角色动态显示菜单项
3. **Clerks.vue** - 员工管理页面添加角色选择和显示功能

### 前端权限控制
- 基于用户角色控制侧边栏菜单显示
- 在员工列表中显示角色标签
- 员工表单支持角色选择

## 使用说明

### 初始管理员账户
系统启动时会自动创建默认管理员账户：
- 用户名：`admin`
- 密码：`admin123`
- 角色：ADMIN（管理员）

### 角色分配
在员工管理页面，可以为员工分配不同的角色：
1. 进入员工管理页面
2. 新增或编辑员工
3. 在角色下拉框中选择相应角色
4. 保存后员工即拥有对应角色的权限

### 权限验证
系统会在两个层面进行权限验证：
1. **前端界面** - 根据角色隐藏或显示相应功能菜单
2. **后端API** - 每个受保护的API都会验证用户角色权限

## 文件清单

### 新增文件
- `src/main/java/com/pet/management/security/Role.java`
- `src/main/java/com/pet/management/security/Permission.java`
- `src/main/java/com/pet/management/controller/ApiRoleController.java`
- `frontend/src/api/role.js`
- `plans/permission-control-implementation.md`

### 修改文件
- `src/main/java/com/pet/management/model/Clerk.java`
- `src/main/java/com/pet/management/security/ClerkDetailsService.java`
- `src/main/java/com/pet/management/controller/ApiClerkController.java`
- `src/main/java/com/pet/management/controller/AuthController.java`
- `src/main/java/com/pet/management/initializer/AdminDataInitializer.java`
- `frontend/src/views/Login.vue`
- `frontend/src/components/Layout.vue`
- `frontend/src/views/Clerks.vue`

