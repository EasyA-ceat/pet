# 宠物管理系统

基于 **Spring Boot + JavaFX + Vue 3** 开发的宠物管理系统桌面应用，支持宠物店日常业务管理、客户管理、财务管理、报表统计等功能。

## 技术栈

### 后端
- **Java 17** - 开发语言
- **Spring Boot 3.1** - 后端框架
- **Spring Security + JWT** - 权限认证
- **Spring Data JPA** - ORM框架
- **SQLite 3** - 嵌入式数据库

### 前端
- **Vue 3** - 前端框架
- **Element Plus** - UI组件库
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Vite** - 构建工具
- **ECharts** - 图表库

## 功能特性

### 🐾 核心业务
- **顾客管理** - 客户信息、宠物信息管理
- **服务管理** - 服务项目、价格配置
- **预约管理** - 服务预约、日程安排
- **交易记录** - 消费记录、流水管理
- **充值管理** - 会员充值、活动配置

### 💰 财务管理
- **收支管理** - 收入支出记录
- **员工提成** - 服务提成计算
- **报表统计** - 日报/月报/年报生成
- **数据导出** - 支持PDF、Excel导出
- **图表展示** - 经营数据可视化

### ⚙️ 系统功能
- **角色权限** - 多角色、细粒度权限控制
- **数据备份** - 自动备份、手动备份、数据恢复
- **系统设置** - 参数配置、个性化设置
- **安全审计** - 操作日志记录

## 环境要求

- **操作系统**：Windows 10/11
- **Java版本**：JDK 17 或更高版本
- **Node版本**：Node.js 16+ (前端开发使用)
- **Maven版本**：3.6+

## 快速开始

### 1. 开发环境运行

#### 后端运行
```bash
# 克隆项目
git clone https://github.com/EasyA-ceat/pet.git
cd pet

# 编译打包
mvn clean package

# 运行项目
mvn spring-boot:run
```

#### 前端运行
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 项目结构

```
pet-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/pet/management/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # REST API控制器
│   │   │   ├── model/           # 实体类
│   │   │   ├── repository/      # JPA仓库
│   │   │   ├── security/        # 权限认证
│   │   │   ├── service/         # 业务逻辑层
│   │   │   └── view/controller/ # JavaFX UI控制器
│   │   └── resources/
│   │       ├── fxml/            # JavaFX界面文件
│   │       ├── css/             # 样式文件
│   │       └── images/          # 图片资源
│   └── test/                    # 单元测试
├── frontend/                    # Vue前端代码
│   ├── src/
│   │   ├── api/                 # API接口封装
│   │   ├── components/          # 公共组件
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── store/               # Pinia状态管理
│   │   └── utils/               # 工具函数
│   └── package.json
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 数据存储

- **数据库文件**：自动存储在 `用户主目录/.pet-management-system/pet_db.sqlite`
- **备份文件**：默认存储在 `用户主目录/.pet-management-system/backup/`
- **配置文件**：`用户主目录/.pet-management-system/settings.properties`

## 构建打包

```bash
# 完整打包（生成jar和exe文件）
mvn clean package

# 输出目录
target/
├── pet-management-system-<version>.jar  # 可执行jar包
└── 宠物管理系统.exe                      # Windows可执行文件
```

## 系统截图

（待补充）

## 开发说明

### 后端开发
- 遵循Spring Boot最佳实践
- 数据库操作使用JPA Repository
- 业务逻辑在Service层实现
- API接口遵循RESTful规范

### 前端开发
- 使用Vue 3组合式API
- UI组件优先使用Element Plus
- API统一封装在 `src/api/` 目录
- 状态管理使用Pinia

## 常见问题

### Q: 运行时提示找不到Java环境？
A: 请安装JDK 17或更高版本，推荐使用 [Eclipse Temurin 17](https://adoptium.net/)

### Q: 数据备份文件在哪里？
A: 默认在 `C:\Users\你的用户名\.pet-management-system\backup\` 目录下

### Q: 如何重置系统？
A: 删除 `用户主目录/.pet-management-system/` 目录，重启系统即可重新初始化

## 技术支持

如有问题，请提交Issue或联系开发团队。

## 许可证

MIT License
