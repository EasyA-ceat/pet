# UI 重新设计实施计划

## 项目概述

本文档详细说明宠物管理系统UI重新设计的技术实施方案，确保在保持所有原有功能的前提下，实现现代化的用户界面。

## 技术架构

### 文件结构变更
```
src/main/resources/
├── css/
│   ├── main.css                    (保留 - 备用)
│   ├── material-design.css         (保留 - 备用)
│   └── modern-design.css           (新增 - 新样式系统)
├── fxml/
│   ├── main.fxml                   (重构 - 新布局)
│   ├── dashboard.fxml              (新增 - 仪表盘)
│   ├── customer.fxml               (重构 - 新设计)
│   ├── finance.fxml                (重构 - 新设计)
│   ├── clerk.fxml                  (重构 - 新设计)
│   ├── report.fxml                 (重构 - 新设计)
│   ├── settings.fxml               (重构 - 新设计)
│   └── about.fxml                  (保持不变)
└── images/                         (保持不变)
```

## 第一阶段：新CSS样式系统

### 1.1 创建 modern-design.css

**核心变量定义**
```css
/* ================================
   现代化设计系统 - 宠物管理系统
   ================================ */

/* 变量定义 */
* {
    /* 主色调 - 温暖珊瑚红 */
    --primary: #FF6B6B;
    --primary-dark: #EE5A5A;
    --primary-light: #FFE5E5;
    
    /* 辅助色 - 清新青绿 */
    --secondary: #4ECDC4;
    --secondary-dark: #3DBDB5;
    --secondary-light: #E0F7F5;
    
    /* 强调色 - 明亮黄色 */
    --accent: #FFE66D;
    --accent-dark: #F4D35E;
    
    /* 中性色 */
    --background: #F8F9FA;
    --surface: #FFFFFF;
    --text-primary: #2D3436;
    --text-secondary: #636E72;
    --text-muted: #B2BEC3;
    --divider: #DFE6E9;
    
    /* 功能色 */
    --success: #00B894;
    --success-light: #D4F4EE;
    --warning: #FDCB6E;
    --warning-light: #FFF3CD;
    --danger: #FF7675;
    --danger-light: #FDEDEC;
    --info: #74B9FF;
    --info-light: #D6EAF8;
}

/* 基础样式 */
.modern-root {
    -fx-font-family: "Microsoft YaHei", "PingFang SC", -apple-system, BlinkMacSystemFont, sans-serif;
    -fx-font-size: 14px;
    -fx-background-color: --background;
}
```

**阴影系统**
```css
/* ================================
   现代化阴影系统
   ================================ */
.shadow-none { -fx-effect: null; }
.shadow-sm {
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 6, 0, 0, 2);
}
.shadow {
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 4);
}
.shadow-md {
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 4);
}
.shadow-lg {
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 8);
}
.shadow-xl {
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 28, 0, 0, 12);
}
```

**圆角系统**
```css
/* ================================
   圆角系统
   ================================ */
.rounded-none { -fx-background-radius: 0; }
.rounded-sm { -fx-background-radius: 4; }
.rounded { -fx-background-radius: 6; }
.rounded-md { -fx-background-radius: 8; }
.rounded-lg { -fx-background-radius: 12; }
.rounded-xl { -fx-background-radius: 16; }
.rounded-2xl { -fx-background-radius: 24; }
.rounded-full { -fx-background-radius: 9999; }
```

**按钮组件**
```css
/* ================================
   按钮组件
   ================================ */
.btn {
    -fx-padding: 10 20;
    -fx-background-radius: 8;
    -fx-cursor: hand;
    -fx-font-size: 14px;
    -fx-font-weight: 500;
    -fx-pref-height: 40;
    -fx-background-insets: 0;
}

.btn-primary {
    -fx-background-color: --primary;
    -fx-text-fill: white;
}
.btn-primary:hover {
    -fx-background-color: --primary-dark;
}

.btn-secondary {
    -fx-background-color: --secondary;
    -fx-text-fill: white;
}

.btn-ghost {
    -fx-background-color: transparent;
    -fx-text-fill: --text-primary;
}
.btn-ghost:hover {
    -fx-background-color: rgba(0,0,0,0.04);
}

.btn-outline {
    -fx-background-color: transparent;
    -fx-border-color: --divider;
    -fx-border-width: 1;
    -fx-text-fill: --text-primary;
}

.btn-small {
    -fx-padding: 6 12;
    -fx-pref-height: 32;
    -fx-font-size: 13px;
}

.btn-large {
    -fx-padding: 14 28;
    -fx-pref-height: 48;
    -fx-font-size: 16px;
}
```

**卡片组件**
```css
/* ================================
   卡片组件
   ================================ */
.card {
    -fx-background-color: --surface;
    -fx-background-radius: 12;
    -fx-padding: 20;
}

.card-hover:hover {
    -fx-translate-y: -2;
}

.card-header {
    -fx-padding: 20 20 0 20;
}

.card-title {
    -fx-font-size: 18px;
    -fx-font-weight: 600;
    -fx-text-fill: --text-primary;
}

.card-description {
    -fx-font-size: 14px;
    -fx-text-fill: --text-secondary;
}

.card-content {
    -fx-padding: 20;
}

.card-footer {
    -fx-padding: 0 20 20 20;
}
```

**输入组件**
```css
/* ================================
   输入组件
   ================================ */
.input {
    -fx-background-color: --surface;
    -fx-border-color: --divider;
    -fx-border-width: 1;
    -fx-border-radius: 8;
    -fx-padding: 10 12;
    -fx-font-size: 14px;
    -fx-pref-height: 42;
}
.input:focused {
    -fx-border-color: --primary;
    -fx-border-width: 2;
    -fx-effect: dropshadow(gaussian, rgba(255,107,107,0.1), 0, 0, 0, 0);
}

.search-input {
    -fx-background-color: rgba(0,0,0,0.04);
    -fx-border-color: transparent;
    -fx-background-radius: 10;
    -fx-padding: 10 16;
    -fx-pref-height: 44;
}
.search-input:focused {
    -fx-background-color: --surface;
    -fx-border-color: --primary;
    -fx-border-width: 1;
}
```

**统计卡片**
```css
/* ================================
   统计卡片
   ================================ */
.stat-card {
    -fx-background-color: --surface;
    -fx-background-radius: 12;
    -fx-padding: 20;
}

.stat-card.primary {
    -fx-background-color: linear-gradient(135deg, --primary 0%, --primary-dark 100%);
}
.stat-card.primary .stat-label,
.stat-card.primary .stat-value,
.stat-card.primary .stat-trend {
    -fx-text-fill: white;
}

.stat-card.success {
    -fx-background-color: linear-gradient(135deg, --success 0%, #00A886 100%);
}
.stat-card.success .stat-label,
.stat-card.success .stat-value,
.stat-card.success .stat-trend {
    -fx-text-fill: white;
}

.stat-label {
    -fx-font-size: 13px;
    -fx-text-fill: --text-secondary;
    -fx-font-weight: 500;
}

.stat-value {
    -fx-font-size: 28px;
    -fx-font-weight: 700;
    -fx-text-fill: --text-primary;
}

.stat-trend {
    -fx-font-size: 13px;
    -fx-font-weight: 500;
}
.stat-trend.up { -fx-text-fill: --success; }
.stat-trend.down { -fx-text-fill: --danger; }
```

**导航组件**
```css
/* ================================
   导航组件
   ================================ */
.modern-header {
    -fx-background-color: --surface;
    -fx-pref-height: 64;
    -fx-padding: 0 24;
    -fx-border-color: --divider;
    -fx-border-width: 0 0 1 0;
}

.brand-icon {
    -fx-font-size: 28;
}

.brand-title {
    -fx-font-size: 20;
    -fx-font-weight: 700;
    -fx-text-fill: --text-primary;
}

.modern-sidebar {
    -fx-background-color: --surface;
    -fx-pref-width: 240;
    -fx-padding: 16 8;
    -fx-border-color: --divider;
    -fx-border-width: 0 1 0 0;
}

.nav-tab {
    -fx-background-color: transparent;
    -fx-text-fill: --text-secondary;
    -fx-alignment: CENTER_LEFT;
    -fx-padding: 12 16;
    -fx-pref-width: 224;
    -fx-pref-height: 44;
    -fx-background-radius: 8;
    -fx-cursor: hand;
    -fx-font-size: 14px;
    -fx-font-weight: 500;
}
.nav-tab:hover {
    -fx-background-color: rgba(0,0,0,0.04);
    -fx-text-fill: --text-primary;
}
.nav-tab.active {
    -fx-background-color: --primary-light;
    -fx-text-fill: --primary-dark;
    -fx-font-weight: 600;
}

.modern-content {
    -fx-background-color: --background;
    -fx-padding: 24;
}
```

**页面布局**
```css
/* ================================
   页面布局
   ================================ */
.page-container {
    -fx-spacing: 24;
}

.page-header {
    -fx-padding: 0 0 8 0;
}

.page-title {
    -fx-font-size: 24px;
    -fx-font-weight: 700;
    -fx-text-fill: --text-primary;
}

.page-subtitle {
    -fx-font-size: 14px;
    -fx-text-fill: --text-secondary;
}

.section-header {
    -fx-padding: 0 0 12 0;
}

.section-title {
    -fx-font-size: 16px;
    -fx-font-weight: 600;
    -fx-text-fill: --text-primary;
}

.filter-bar {
    -fx-background-color: --surface;
    -fx-background-radius: 12;
    -fx-padding: 16;
}
```

**表格样式**
```css
/* ================================
   表格样式
   ================================ */
.modern-table {
    -fx-background-color: --surface;
    -fx-background-radius: 12;
}

.modern-table .column-header-background {
    -fx-background-color: --background;
    -fx-background-radius: 12 12 0 0;
}

.modern-table .column-header {
    -fx-background-color: transparent;
    -fx-border-width: 0 0 1 0;
    -fx-border-color: --divider;
    -fx-padding: 14 16;
}

.modern-table .column-header .label {
    -fx-font-weight: 600;
    -fx-text-fill: --text-secondary;
    -fx-font-size: 12px;
}

.modern-table .table-cell {
    -fx-padding: 14 16;
    -fx-border-width: 0 0 1 0;
    -fx-border-color: --divider;
    -fx-text-fill: --text-primary;
}

.modern-table .table-row-cell {
    -fx-background-color: transparent;
    -fx-cursor: hand;
}

.modern-table .table-row-cell:hover {
    -fx-background-color: rgba(0,0,0,0.02);
}

.modern-table .table-row-cell:selected {
    -fx-background-color: --primary-light;
}

.modern-table .table-row-cell:selected .table-cell {
    -fx-text-fill: --primary-dark;
}
```

**顾客卡片**
```css
/* ================================
   顾客卡片
   ================================ */
.customer-card {
    -fx-background-color: --surface;
    -fx-background-radius: 12;
    -fx-padding: 16;
    -fx-cursor: hand;
}
.customer-card:hover {
    -fx-background-color: rgba(0,0,0,0.02);
}

.customer-avatar {
    -fx-pref-width: 48;
    -fx-pref-height: 48;
    -fx-background-radius: 24;
    -fx-background-color: --primary-light;
}

.customer-name {
    -fx-font-size: 16px;
    -fx-font-weight: 600;
    -fx-text-fill: --text-primary;
}

.customer-tag {
    -fx-background-radius: 4;
    -fx-padding: 2 8;
    -fx-font-size: 11px;
    -fx-font-weight: 600;
}
.customer-tag.vip {
    -fx-background-color: --accent;
    -fx-text-fill: #744210;
}

.customer-phone {
    -fx-font-size: 13px;
    -fx-text-fill: --text-secondary;
}

.pet-section {
    -fx-background-color: --background;
    -fx-background-radius: 8;
    -fx-padding: 12;
}

.pet-icon {
    -fx-font-size: 24;
}

.pet-name {
    -fx-font-size: 14px;
    -fx-font-weight: 500;
    -fx-text-fill: --text-primary;
}

.pet-info {
    -fx-font-size: 12px;
    -fx-text-fill: --text-secondary;
}

.last-visit {
    -fx-font-size: 12px;
    -fx-text-fill: --text-muted;
}
```

## 第二阶段：主界面重构

### 2.1 新的 main.fxml 布局

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.text.*?>

<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.pet.management.view.controller.MainController"
            prefWidth="1440" prefHeight="900"
            styleClass="modern-root">
    
    <!-- 顶部导航栏 -->
    <top>
        <HBox styleClass="modern-header shadow-sm" alignment="CENTER_LEFT" spacing="20">
            
            <!-- 品牌标识 -->
            <HBox alignment="CENTER_LEFT" spacing="12">
                <Label styleClass="brand-icon" text="🐾" />
                <Label styleClass="brand-title" text="宠物管家" />
            </HBox>
            
            <!-- 全局搜索 -->
            <HBox styleClass="search-section" alignment="CENTER_LEFT" HBox.hgrow="ALWAYS">
                <TextField fx:id="globalSearchField" promptText="搜索顾客、宠物、交易..." 
                           styleClass="search-input" prefWidth="480" />
            </HBox>
            
            <!-- 快捷操作 -->
            <HBox styleClass="quick-actions" alignment="CENTER_LEFT" spacing="12">
                <Button fx:id="quickAddCustomer" text="➕ 新顾客" styleClass="btn btn-secondary" />
                <Button fx:id="quickAddTransaction" text="💰 新交易" styleClass="btn btn-primary" />
            </HBox>
            
            <!-- 时间和设置 -->
            <HBox styleClass="user-section" alignment="CENTER_LEFT" spacing="16">
                <VBox alignment="CENTER_RIGHT" spacing="2">
                    <Label fx:id="currentTime" styleClass="time-display" text="12:00">
                        <font>
                            <Font size="18" name="Microsoft YaHei" />
                        </font>
                    </Label>
                    <Label fx:id="currentDate" styleClass="date-display" text="2024-01-01">
                        <font>
                            <Font size="12" name="Microsoft YaHei" />
                        </font>
                    </Label>
                </VBox>
                <Button fx:id="settingsBtn" styleClass="btn btn-ghost btn-small" text="⚙️" />
                <Button fx:id="aboutBtn" styleClass="btn btn-ghost btn-small" text="ℹ️" />
            </HBox>
        </HBox>
    </top>
    
    <!-- 左侧导航 -->
    <left>
        <VBox styleClass="modern-sidebar" spacing="4">
            
            <!-- 导航标签 -->
            <VBox styleClass="nav-tabs" spacing="4" VBox.vgrow="ALWAYS">
                <Button fx:id="navDashboard" styleClass="nav-tab active"
                        onAction="#handleDashboard" text="📊 仪表盘" />
                <Button fx:id="navCustomer" styleClass="nav-tab"
                        onAction="#handleCustomerManagement" text="👥 顾客" />
                <Button fx:id="navFinance" styleClass="nav-tab"
                        onAction="#handleFinanceManagement" text="💰 财务" />
                <Button fx:id="navClerk" styleClass="nav-tab"
                        onAction="#handleClerkManagement" text="👨‍💼 员工" />
                <Button fx:id="navReport" styleClass="nav-tab"
                        onAction="#handleReports" text="📈 报表" />
            </VBox>
            
            <!-- 底部分隔 -->
            <Separator styleClass="nav-divider">
                <padding>
                    <Insets top="8" bottom="8" />
                </padding>
            </Separator>
            
            <!-- 底部设置 -->
            <VBox styleClass="nav-footer" spacing="4">
                <Button fx:id="navSettings" styleClass="nav-tab"
                        onAction="#handleSettings" text="⚙️ 设置" />
            </VBox>
        </VBox>
    </left>
    
    <!-- 主内容区域 -->
    <center>
        <StackPane fx:id="contentPane" styleClass="modern-content" />
    </center>
    
</BorderPane>
```

### 2.2 更新 MainController.java

需要在 MainController 中添加以下方法：

```java
// 仪表盘处理
@FXML
public void handleDashboard() {
    showDashboard();
    updateStatus("仪表盘已加载");
}

// 显示仪表盘
private void showDashboard() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Parent dashboardView = loader.load();
        contentPane.getChildren().setAll(dashboardView);
        updateActiveNavButton("dashboard");
    } catch (IOException e) {
        showErrorDialog("加载仪表盘失败", e.getMessage());
        e.printStackTrace();
    }
}

// 更新导航按钮状态 - 添加dashboard选项
private void updateActiveNavButton(String activePage) {
    // 重置所有导航按钮样式
    if (navDashboard != null) navDashboard.getStyleClass().remove("active");
    if (navCustomer != null) navCustomer.getStyleClass().remove("active");
    if (navFinance != null) navFinance.getStyleClass().remove("active");
    if (navClerk != null) navClerk.getStyleClass().remove("active");
    if (navReport != null) navReport.getStyleClass().remove("active");
    if (navSettings != null) navSettings.getStyleClass().remove("active");
    
    // 设置当前页面按钮为激活状态
    switch (activePage) {
        case "dashboard":
            if (navDashboard != null) navDashboard.getStyleClass().add("active");
            break;
        case "customer":
            if (navCustomer != null) navCustomer.getStyleClass().add("active");
            break;
        case "finance":
            if (navFinance != null) navFinance.getStyleClass().add("active");
            break;
        case "clerk":
            if (navClerk != null) navClerk.getStyleClass().add("active");
            break;
        case "reports":
            if (navReport != null) navReport.getStyleClass().add("active");
            break;
        case "settings":
            if (navSettings != null) navSettings.getStyleClass().add("active");
            break;
    }
}

// 修改 initialize 方法，默认显示仪表盘
@FXML
public void initialize() {
    // 初始化时钟
    initializeClock();
    
    // 默认显示仪表盘
    showDashboard();
    
    // 设置状态信息
    updateStatus("欢迎使用宠物管家");
}
```

## 第三阶段：仪表盘页面

### 3.1 创建 dashboard.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.text.*?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      styleClass="page-container"
      spacing="24">
    <padding>
        <Insets top="0" right="0" bottom="0" left="0" />
    </padding>
    
    <!-- 欢迎区域 -->
    <HBox styleClass="card shadow-md" alignment="CENTER_LEFT">
        <padding>
            <Insets top="24" right="24" bottom="24" left="24" />
        </padding>
        <VBox spacing="8" HBox.hgrow="ALWAYS">
            <Label styleClass="page-title" text="早上好！👋" />
            <Label styleClass="page-subtitle" text="今天是美好的一天，让我们开始工作吧" />
        </VBox>
        <VBox spacing="8" alignment="CENTER_RIGHT">
            <Label text="本周目标完成度" styleClass="page-subtitle" />
            <ProgressBar fx:id="weeklyProgress" progress="0.65" prefWidth="200" />
            <Label text="65% (13/20)" styleClass="stat-label" />
        </VBox>
    </HBox>
    
    <!-- 统计卡片 -->
    <GridPane styleClass="stats-grid" hgap="16" vgap="16">
        <columnConstraints>
            <ColumnConstraints hgrow="ALWAYS" percentWidth="25" />
            <ColumnConstraints hgrow="ALWAYS" percentWidth="25" />
            <ColumnConstraints hgrow="ALWAYS" percentWidth="25" />
            <ColumnConstraints hgrow="ALWAYS" percentWidth="25" />
        </columnConstraints>
        
        <!-- 今日营收 -->
        <VBox styleClass="stat-card primary shadow-md" GridPane.columnIndex="0" GridPane.rowIndex="0">
            <HBox alignment="CENTER_LEFT" spacing="16">
                <VBox spacing="4">
                    <Label styleClass="stat-label" text="今日营收" />
                    <Label fx:id="todayRevenue" styleClass="stat-value" text="¥12,580" />
                    <HBox alignment="CENTER_LEFT" spacing="4">
                        <Label styleClass="stat-trend up" text="↑ 12.5%" />
                        <Label styleClass="stat-label" text="较昨日" />
                    </HBox>
                </VBox>
                <Region HBox.hgrow="ALWAYS" />
                <VBox styleClass="stat-icon-wrapper" alignment="CENTER">
                    <Label text="💰" styleClass="stat-icon-lg" />
                </VBox>
            </HBox>
        </VBox>
        
        <!-- 活跃顾客 -->
        <VBox styleClass="stat-card success shadow-md" GridPane.columnIndex="1" GridPane.rowIndex="0">
            <HBox alignment="CENTER_LEFT" spacing="16">
                <VBox spacing="4">
                    <Label styleClass="stat-label" text="活跃顾客" />
                    <Label fx:id="activeCustomers" styleClass="stat-value" text="156" />
                    <HBox alignment="CENTER_LEFT" spacing="4">
                        <Label styleClass="stat-trend up" text="↑ 8" />
                        <Label styleClass="stat-label" text="较上周" />
                    </HBox>
                </VBox>
                <Region HBox.hgrow="ALWAYS" />
                <VBox styleClass="stat-icon-wrapper" alignment="CENTER">
                    <Label text="👥" styleClass="stat-icon-lg" />
                </VBox>
            </HBox>
        </VBox>
        
        <!-- 待处理事项 -->
        <VBox styleClass="stat-card shadow-md" GridPane.columnIndex="2" GridPane.rowIndex="0">
            <HBox alignment="CENTER_LEFT" spacing="16">
                <VBox spacing="4">
                    <Label styleClass="stat-label" text="待处理" />
                    <Label fx:id="pendingTasks" styleClass="stat-value" text="12" />
                    <HBox alignment="CENTER_LEFT" spacing="4">
                        <Label styleClass="stat-label" text="需要关注" />
                    </HBox>
                </VBox>
                <Region HBox.hgrow="ALWAYS" />
                <VBox styleClass="stat-icon-wrapper warning" alignment="CENTER">
                    <Label text="📋" styleClass="stat-icon-lg" />
                </VBox>
            </HBox>
        </VBox>
        
        <!-- 本月目标 -->
        <VBox styleClass="stat-card shadow-md" GridPane.columnIndex="3" GridPane.rowIndex="0">
            <HBox alignment="CENTER_LEFT" spacing="16">
                <VBox spacing="4">
                    <Label styleClass="stat-label" text="本月目标" />
                    <Label fx:id="monthlyTarget" styleClass="stat-value" text="¥185,000" />
                    <HBox alignment="CENTER_LEFT" spacing="4">
                        <Label styleClass="stat-label" text="已完成 62%" />
                    </HBox>
                </VBox>
                <Region HBox.hgrow="ALWAYS" />
                <VBox styleClass="stat-icon-wrapper info" alignment="CENTER">
                    <Label text="🎯" styleClass="stat-icon-lg" />
                </VBox>
            </HBox>
        </VBox>
    </GridPane>
    
    <!-- 下方内容区域 -->
    <HBox spacing="16" HBox.hgrow="ALWAYS">
        
        <!-- 左侧：快速操作和最近顾客 -->
        <VBox spacing="16" HBox.hgrow="ALWAYS">
            
            <!-- 快速操作 -->
            <VBox styleClass="card shadow-md" spacing="16">
                <Label styleClass="card-title" text="快速操作" />
                <GridPane hgap="12" vgap="12">
                    <columnConstraints>
                        <ColumnConstraints hgrow="ALWAYS" percentWidth="50" />
                        <ColumnConstraints hgrow="ALWAYS" percentWidth="50" />
                    </columnConstraints>
                    
                    <Button styleClass="btn btn-primary btn-large" text="➕ 登记新顾客" GridPane.columnIndex="0" GridPane.rowIndex="0" />
                    <Button styleClass="btn btn-secondary btn-large" text="💰 记录新交易" GridPane.columnIndex="1" GridPane.rowIndex="0" />
                    <Button styleClass="btn btn-outline btn-large" text="📊 查看日报表" GridPane.columnIndex="0" GridPane.rowIndex="1" />
                    <Button styleClass="btn btn-outline btn-large" text="📋 预约管理" GridPane.columnIndex="1" GridPane.rowIndex="1" />
                </GridPane>
            </VBox>
            
            <!-- 最近顾客 -->
            <VBox styleClass="card shadow-md" spacing="16" VBox.vgrow="ALWAYS">
                <HBox alignment="CENTER_LEFT">
                    <Label styleClass="card-title" text="最近到访顾客" />
                    <Region HBox.hgrow="ALWAYS" />
                    <Button styleClass="btn btn-ghost btn-small" text="查看全部 →" />
                </HBox>
                
                <VBox spacing="12">
                    <!-- 顾客列表占位 -->
                    <HBox styleClass="customer-card" alignment="CENTER_LEFT" spacing="12">
                        <Region styleClass="customer-avatar" />
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="customer-name" text="张三 · 毛毛" />
                            <Label styleClass="customer-phone" text="金毛犬 · 今天上午" />
                        </VBox>
                        <Label styleClass="customer-tag vip" text="VIP" />
                    </HBox>
                    
                    <HBox styleClass="customer-card" alignment="CENTER_LEFT" spacing="12">
                        <Region styleClass="customer-avatar" />
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="customer-name" text="李四 · 咪咪" />
                            <Label styleClass="customer-phone" text="英国短毛猫 · 昨天" />
                        </VBox>
                    </HBox>
                    
                    <HBox styleClass="customer-card" alignment="CENTER_LEFT" spacing="12">
                        <Region styleClass="customer-avatar" />
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="customer-name" text="王五 · 旺财" />
                            <Label styleClass="customer-phone" text="柯基犬 · 2天前" />
                        </VBox>
                    </HBox>
                </VBox>
            </VBox>
        </VBox>
        
        <!-- 右侧：最近交易和营收趋势 -->
        <VBox spacing="16" prefWidth="420">
            
            <!-- 营收趋势小卡片 -->
            <VBox styleClass="card shadow-md" spacing="16">
                <Label styleClass="card-title" text="营收趋势" />
                <VBox styleClass="chart-placeholder" alignment="CENTER">
                    <Label text="📈" styleClass="chart-placeholder-icon" />
                    <Label styleClass="page-subtitle" text="最近7天营收趋势图" />
                </VBox>
            </VBox>
            
            <!-- 最近交易 -->
            <VBox styleClass="card shadow-md" spacing="16" VBox.vgrow="ALWAYS">
                <HBox alignment="CENTER_LEFT">
                    <Label styleClass="card-title" text="最近交易" />
                    <Region HBox.hgrow="ALWAYS" />
                    <Button styleClass="btn btn-ghost btn-small" text="查看全部 →" />
                </HBox>
                
                <VBox spacing="12">
                    <!-- 交易列表占位 -->
                    <HBox styleClass="transaction-item" alignment="CENTER_LEFT" spacing="12">
                        <VBox styleClass="transaction-icon success" alignment="CENTER">
                            <Label text="💰" />
                        </VBox>
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="transaction-title" text="洗澡服务" />
                            <Label styleClass="transaction-subtitle" text="张三 · 10:30" />
                        </VBox>
                        <Label styleClass="transaction-amount" text="+¥128" />
                    </HBox>
                    
                    <HBox styleClass="transaction-item" alignment="CENTER_LEFT" spacing="12">
                        <VBox styleClass="transaction-icon primary" alignment="CENTER">
                            <Label text="✂️" />
                        </VBox>
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="transaction-title" text="美容造型" />
                            <Label styleClass="transaction-subtitle" text="李四 · 昨天" />
                        </VBox>
                        <Label styleClass="transaction-amount" text="+¥268" />
                    </HBox>
                    
                    <HBox styleClass="transaction-item" alignment="CENTER_LEFT" spacing="12">
                        <VBox styleClass="transaction-icon info" alignment="CENTER">
                            <Label text="💊" />
                        </VBox>
                        <VBox spacing="2" HBox.hgrow="ALWAYS">
                            <Label styleClass="transaction-title" text="医疗检查" />
                            <Label styleClass="transaction-subtitle" text="王五 · 昨天" />
                        </VBox>
                        <Label styleClass="transaction-amount" text="+¥380" />
                    </HBox>
                </VBox>
            </VBox>
        </VBox>
    </HBox>
    
</VBox>
```

## 第四阶段：顾客管理页面重构

### 4.1 新的 customer.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.text.*?>
<?import javafx.scene.control.cell.PropertyValueFactory?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.pet.management.view.controller.CustomerController"
      styleClass="page-container"
      spacing="20">
    <padding>
        <Insets top="0" right="0" bottom="0" left="0" />
    </padding>
    
    <!-- 页面标题与操作 -->
    <HBox styleClass="page-header" alignment="CENTER_LEFT">
        <VBox spacing="4">
            <Label styleClass="page-title" text="顾客管理" />
            <Label styleClass="page-subtitle" text="管理您的顾客信息和宠物档案" />
        </VBox>
        <Region HBox.hgrow="ALWAYS" />
        <HBox spacing="12">
            <Button styleClass="btn btn-outline" text="📥 导入" />
            <Button fx:id="addBtn" styleClass="btn btn-primary" text="➕ 添加顾客" />
        </HBox>
    </HBox>
    
    <!-- 筛选与搜索 -->
    <HBox styleClass="filter-bar shadow-sm" spacing="12" alignment="CENTER_LEFT">
        <TextField fx:id="searchField" styleClass="search-input" promptText="搜索顾客姓名、电话、宠物名..." prefWidth="360" />
        <ComboBox fx:id="petTypeFilter" styleClass="input" promptText="宠物类型" prefWidth="140" />
        <ComboBox fx:id="activityFilter" styleClass="input" promptText="活跃度" prefWidth="140" />
        <Region HBox.hgrow="ALWAYS" />
        <Button fx:id="resetFilter" styleClass="btn btn-ghost" text="重置筛选" />
        <Button fx:id="refreshBtn" styleClass="btn btn-secondary" text="🔄 刷新" />
    </HBox>
    
    <!-- 统计摘要 -->
    <GridPane hgap="16">
        <columnConstraints>
            <ColumnConstraints hgrow="ALWAYS" percentWidth="33.33" />
            <ColumnConstraints hgrow="ALWAYS" percentWidth="33.33" />
            <ColumnConstraints hgrow="ALWAYS" percentWidth="33.33" />
        </columnConstraints>
        
        <VBox styleClass="card shadow-sm" spacing="8" GridPane.columnIndex="0">
            <Label styleClass="stat-label" text="总顾客数" />
            <Label fx:id="totalCustomersLabel" styleClass="stat-value" text="0" />
        </VBox>
        
        <VBox styleClass="card shadow-sm" spacing="8" GridPane.columnIndex="1">
            <Label styleClass="stat-label" text="活跃顾客" />
            <Label fx:id="activeCustomersLabel" styleClass="stat-value" text="0" />
        </VBox>
        
        <VBox styleClass="card shadow-sm" spacing="8" GridPane.columnIndex="2">
            <Label styleClass="stat-label" text="本月新增" />
            <Label fx:id="recentCustomersLabel" styleClass="stat-value" text="0" />
        </VBox>
    </GridPane>
    
    <!-- 视图切换和内容 -->
    <VBox styleClass="card shadow-md" spacing="0" VBox.vgrow="ALWAYS">
        <padding>
            <Insets top="16" right="16" bottom="0" left="16" />
        </padding>
        
        <!-- 视图切换 -->
        <HBox alignment="CENTER_LEFT" spacing="8">
            <ToggleGroup fx:id="viewToggleGroup">
                <ToggleButton fx:id="listViewBtn" styleClass="btn btn-ghost btn-small" text="📋 列表" selected="true" />
                <ToggleButton fx:id="cardViewBtn" styleClass="btn btn-ghost btn-small" text="🃏 卡片" />
            </ToggleGroup>
            <Region HBox.hgrow="ALWAYS" />
            <Label fx:id="statusLabel" styleClass="stat-label" text="就绪">
                <graphic>
                    <Region styleClass="status-dot status-success" />
                </graphic>
            </Label>
        </HBox>
        
        <Separator />
        
        <!-- 列表视图 -->
        <StackPane fx:id="listViewContainer" VBox.vgrow="ALWAYS">
            <TableView fx:id="customerTable" styleClass="modern-table" VBox.vgrow="ALWAYS">
                <columns>
                    <TableColumn fx:id="idColumn" text="ID" prefWidth="70">
                        <cellValueFactory>
                            <PropertyValueFactory property="id" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="customerNameColumn" text="顾客姓名" prefWidth="120">
                        <cellValueFactory>
                            <PropertyValueFactory property="customerName" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="phoneColumn" text="联系电话" prefWidth="140">
                        <cellValueFactory>
                            <PropertyValueFactory property="phone" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="petNameColumn" text="宠物姓名" prefWidth="100">
                        <cellValueFactory>
                            <PropertyValueFactory property="petName" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="petTypeColumn" text="宠物类型" prefWidth="90">
                        <cellValueFactory>
                            <PropertyValueFactory property="petType" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="petBreedColumn" text="宠物品种" prefWidth="110">
                        <cellValueFactory>
                            <PropertyValueFactory property="petBreed" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="petAgeColumn" text="宠物年龄" prefWidth="80">
                        <cellValueFactory>
                            <PropertyValueFactory property="petAge" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn fx:id="createTimeColumn" text="创建时间" prefWidth="170">
                        <cellValueFactory>
                            <PropertyValueFactory property="createTime" />
                        </cellValueFactory>
                    </TableColumn>
                    <TableColumn text="操作" prefWidth="160">
                        <cellValueFactory>
                            <PropertyValueFactory property="id" />
                        </cellValueFactory>
                    </TableColumn>
                </columns>
            </TableView>
        </StackPane>
        
        <!-- 卡片视图 (默认隐藏) -->
        <FlowPane fx:id="cardViewContainer" styleClass="card-view-container" hgap="16" vgap="16" VBox.vgrow="ALWAYS" visible="false" />
    </VBox>
    
</VBox>
```

需要为 customer.fxml 添加的额外CSS：
```css
/* 顾客管理特有样式 */
.view-toggle {
    -fx-background-color: --background;
    -fx-background-radius: 8;
    -fx-padding: 4;
}

.card-view-container {
    -fx-padding: 16;
    -fx-hgap: 16;
    -fx-vgap: 16;
}

.chart-placeholder {
    -fx-background-color: --background;
    -fx-background-radius: 8;
    -fx-pref-height: 160;
}

.chart-placeholder-icon {
    -fx-font-size: 48;
}

.transaction-item {
    -fx-padding: 12;
    -fx-background-radius: 8;
}
.transaction-item:hover {
    -fx-background-color: --background;
}

.transaction-icon {
    -fx-pref-width: 40;
    -fx-pref-height: 40;
    -fx-background-radius: 8;
}
.transaction-icon.success { -fx-background-color: --success-light; }
.transaction-icon.primary { -fx-background-color: --primary-light; }
.transaction-icon.info { -fx-background-color: --info-light; }
.transaction-icon .label { -fx-font-size: 20; }

.transaction-title {
    -fx-font-size: 14px;
    -fx-font-weight: 500;
    -fx-text-fill: --text-primary;
}

.transaction-subtitle {
    -fx-font-size: 12px;
    -fx-text-fill: --text-secondary;
}

.transaction-amount {
    -fx-font-size: 16px;
    -fx-font-weight: 600;
    -fx-text-fill: --success;
}

.status-dot {
    -fx-pref-width: 8;
    -fx-pref-height: 8;
    -fx-background-radius: 4;
}
.status-success { -fx-background-color: --success; }
.status-warning { -fx-background-color: --warning; }
.status-danger { -fx-background-color: --danger; }
.status-info { -fx-background-color: --info; }

.stat-icon-wrapper {
    -fx-pref-width: 56;
    -fx-pref-height: 56;
    -fx-background-radius: 12;
    -fx-background-color: rgba(255,255,255,0.2);
}
.stat-icon-wrapper.warning { -fx-background-color: --warning-light; }
.stat-icon-wrapper.info { -fx-background-color: --info-light; }

.stat-icon-lg {
    -fx-font-size: 28;
}

.time-display {
    -fx-text-fill: --text-primary;
    -fx-font-weight: 600;
}

.date-display {
    -fx-text-fill: --text-secondary;
}

.nav-divider {
    -fx-background-color: --divider;
    -fx-pref-height: 1;
}
```

## 实施注意事项

### 保持兼容性的策略

1. **渐进式迁移**：先创建新文件，测试通过后再替换旧文件
2. **备份原有文件**：重命名而非删除旧的FXML和CSS文件
3. **控制器复用**：尽量复用现有的Controller逻辑，只修改UI绑定部分
4. **功能验证**：每完成一个页面，立即验证所有原有功能是否正常

### 文件备份清单

在开始实施前，备份以下文件：
- `src/main/resources/fxml/main.fxml` → `main.fxml.bak`
- `src/main/resources/fxml/customer.fxml` → `customer.fxml.bak`
- `src/main/resources/fxml/finance.fxml` → `finance.fxml.bak`
- `src/main/resources/fxml/clerk.fxml` → `clerk.fxml.bak`
- `src/main/resources/fxml/report.fxml` → `report.fxml.bak`
- `src/main/resources/fxml/settings.fxml` → `settings.fxml.bak`

### 关键检查点

- [ ] 所有导航功能正常工作
- [ ] 数据加载和显示正常
- [ ] 表单提交和验证正常
- [ ] 搜索和筛选功能正常
- [ ] 表格操作（编辑、删除等）正常
- [ ] 报表生成功能正常
- [ ] PDF导出功能正常
- [ ] 图片上传和管理功能正常
