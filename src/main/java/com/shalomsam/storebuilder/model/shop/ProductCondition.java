package com.shalomsam.storebuilder.model.shop;

import lombok.Getter;

@Getter
public enum ProductCondition {
    NEW("new"),
    USED("used"),
    REFURBISHED("refurbished");

    private final String displayName;

    ProductCondition(String displayName) {
        this.displayName = displayName;
    }
}
