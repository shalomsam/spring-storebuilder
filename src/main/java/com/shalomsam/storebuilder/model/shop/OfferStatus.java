package com.shalomsam.storebuilder.model.shop;

import lombok.Getter;

@Getter
public enum OfferStatus {
    PROCESSING("processing"),
    INACTIVE("inactive"),
    APPROVED("approved"),
    DECLINED("declined");

    private final String displayName;

    OfferStatus(String displayName) {
        this.displayName = displayName;
    }
}
