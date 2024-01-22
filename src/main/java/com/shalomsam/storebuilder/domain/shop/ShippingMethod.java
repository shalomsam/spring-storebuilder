package com.shalomsam.storebuilder.domain.shop;

import lombok.Getter;

@Getter
public enum ShippingMethod {
    STANDARD("standard"),
    EXPRESS("express");

    private final String displayName;

    ShippingMethod(String displayName) {
        this.displayName = displayName;
    }
}
