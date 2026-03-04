package com.pet.management.model;

public enum PaymentMethod {
    CASH("现金"),
    STORED_VALUE("储值扣款"),
    MEITUAN("美团团购券"),
    DOUYIN("抖音团购券");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMethod fromDisplayName(String displayName) {
        for (PaymentMethod method : values()) {
            if (method.displayName.equals(displayName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + displayName);
    }
}
