package com.shalomsam.storebuilder.domain.shop;

import lombok.Getter;

@Getter
public enum CartStatus {
    INITIAL("initial"),
    STORED("stored"),
    COMPLETED("completed"),
    ABANDONED("abandoned");

    private final String displayName;

    CartStatus(String displayName) {
        this.displayName = displayName;
    }
}
