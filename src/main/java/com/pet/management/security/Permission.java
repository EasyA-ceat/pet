
package com.pet.management.security;

public enum Permission {
    // 顾客管理权限
    CUSTOMER_READ("顾客查看"),
    CUSTOMER_WRITE("顾客管理"),
    
    // 员工管理权限
    CLERK_READ("员工查看"),
    CLERK_WRITE("员工管理"),
    
    // 交易管理权限
    TRANSACTION_READ("交易查看"),
    TRANSACTION_WRITE("交易管理"),
    
    // 预约管理权限
    APPOINTMENT_READ("预约查看"),
    APPOINTMENT_WRITE("预约管理"),
    
    // 报告权限
    REPORT_READ("报告查看"),
    REPORT_WRITE("报告生成"),
    
    // 系统管理权限
    SETTINGS_READ("设置查看"),
    SETTINGS_WRITE("设置管理"),
    
    // 仪表盘权限
    DASHBOARD_READ("仪表盘查看");

    private final String displayName;

    Permission(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

