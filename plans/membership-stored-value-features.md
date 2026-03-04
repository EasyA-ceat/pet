# 会员储值及相关功能实现计划

## 概述
本计划描述了四个核心功能点的实现方案：
1. 会员储值功能及活动配置
2. 多种支付方式支持
3. 服务类型固定选项
4. 员工按服务类型配置抽成比例

---

## 功能点1：会员储值功能

### 数据模型设计
- **修改 Customer 实体** (添加会员相关字段):
  - 新增 `isVip` 字段 (Boolean, VIP标识)
  - 新增 `balance` 字段 (BigDecimal, 会员余额)
  - 新增 `totalRecharge` 字段 (BigDecimal, 累计充值金额)
  
- **新增实体**: `RechargeActivity` (充值活动)
  - id, name, min_amount, bonus_amount, start_date, end_date, is_active, create_time
  
- **新增实体**: `RechargeRecord` (充值记录)
  - id, customer_id, amount, bonus_amount, activity_id, payment_method, create_time

### 后端实现
1. 创建 JPA 实体类
2. 创建 Repository 接口
3. 创建 Service 层处理业务逻辑
4. 创建 API Controller 提供 REST 接口
5. 充值时自动应用活动优惠

### 前端实现
1. 新增储值管理页面
2. 充值活动配置界面
3. 会员余额显示
4. 充值记录查询

---

## 功能点2：多种支付方式

### 数据模型修改
- **修改 Transaction 实体**:
  - 新增 `paymentMethod` 字段 (枚举类型: CASH, STORED_VALUE, MEITUAN, DOUYIN)
  - 新增 `storedValueUsed` 字段 (储值扣款金额)

### 后端实现
1. 更新 Transaction 实体
2. 创建支付方式枚举
3. 修改 TransactionService 支持多种支付方式
4. 储值扣款时验证余额并更新余额
5. 更新 API 接口

### 前端实现
1. 交易页面添加支付方式下拉选择
2. 选择储值支付时显示可用余额
3. 显示支付金额明细

---

## 功能点3：服务类型固定选项

### 数据模型修改
- **修改 Transaction 实体**:
  - serviceType 改为枚举类型或使用约束

### 后端实现
1. 创建 ServiceType 枚举:
   - WASH_CARE (洗护)
   - BEAUTY (美容)
   - PARTIAL_TRIM (局部修剪)
   - OTHER_SERVICE (其他服务)
   - FOSTER_CARE (寄养)
2. 更新 Transaction 实体
3. 提供获取服务类型列表的 API

### 前端实现
1. 交易页面使用固定下拉选项
2. 移除手动输入服务类型功能

---

## 功能点4：员工按服务类型配置抽成

### 数据模型设计
- **新增实体**: `ClerkServiceCommission` (员工服务抽成配置)
  - id, clerk_id, service_type, commission_rate, create_time, update_time

### 后端实现
1. 创建 JPA 实体类
2. 创建 Repository 接口
3. 创建 Service 层
4. 创建 API Controller
5. 修改 TransactionService 计算抽成逻辑：
   - 优先使用服务类型特定的抽成比例
   - 如未配置则使用员工默认抽成比例

### 前端实现
1. 员工管理页面添加抽成配置界面
2. 按服务类型设置不同的抽成比例
3. 显示抽成配置列表

---

## 实施顺序
1. 功能点3（服务类型固定选项）- 基础准备
2. 功能点1（会员储值功能）- 核心功能
3. 功能点2（多种支付方式）- 依赖储值功能
4. 功能点4（按服务类型抽成）- 依赖服务类型枚举

---

## 技术要点
- 使用 Spring Boot + JPA 进行后端开发
- 使用 Vue 3 + Vite 进行前端开发
- 数据库使用 SQLite，通过 JPA 自动更新 schema
- 保持 REST API 风格一致性
- 前后端分离架构
