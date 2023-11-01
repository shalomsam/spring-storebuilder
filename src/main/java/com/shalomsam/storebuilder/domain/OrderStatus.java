package com.shalomsam.storebuilder.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    SUBMITTED("submitted"),
    PROCESSING("processing"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    RETURNED("returned");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
