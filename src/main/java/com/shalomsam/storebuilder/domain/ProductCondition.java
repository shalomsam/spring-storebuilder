package com.shalomsam.storebuilder.domain;

import lombok.Getter;

import java.awt.*;

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
