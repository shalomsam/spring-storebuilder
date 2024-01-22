package com.shalomsam.storebuilder.domain.shop;

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
