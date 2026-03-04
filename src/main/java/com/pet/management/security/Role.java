
package com.pet.management.security;

public enum Role {
    ADMIN("管理员", "拥有系统所有权限"),
    MANAGER("经理", "管理员工和查看报告"),
    STAFF("员工", "处理日常业务操作");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

