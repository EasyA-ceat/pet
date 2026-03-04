package com.pet.management.model;

public enum ServiceType {
    WASH_CARE("洗护"),
    BEAUTY("美容"),
    PARTIAL_TRIM("局部修剪"),
    OTHER_SERVICE("其他服务"),
    FOSTER_CARE("寄养");

    private final String displayName;

    ServiceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ServiceType fromDisplayName(String displayName) {
        for (ServiceType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown service type: " + displayName);
    }
}
