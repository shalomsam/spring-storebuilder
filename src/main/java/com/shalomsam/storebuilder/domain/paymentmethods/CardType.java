package com.shalomsam.storebuilder.domain.paymentmethods;

import lombok.Getter;

@Getter
public enum CardType {
    CREDIT("credit"),
    DEBIT("debit"),
    PREPAID("prepaid"),
    UNKNOWN("unknown");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

}
