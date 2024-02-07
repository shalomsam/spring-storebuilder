package com.shalomsam.storebuilder.model.shop;

import lombok.Getter;

@Getter
public enum OrderStatus {
    SUBMITTED("submitted"),
    PROCESSING("processing"),
    SHIPPED_IN_PROGRESS("shipping_in_progress"),
    DELIVERED("delivered"),
    RECEIVED("received"),
    RETURNED("returned");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
