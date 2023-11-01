package com.shalomsam.storebuilder.domain.paymentmethods;

import lombok.Getter;

@Getter
public enum CardBrandTypes {
    AMEX("amex"),
    VISA("visa"),
    MASTERCARD("mastercard"),
    UNIONPAY("unionpay"),
    DINERS("diners"),
    DISCOVERY("discovery"),
    UNKNOWN("unknown");

    private final String displayName;

    CardBrandTypes(String displayName) {
        this.displayName = displayName;
    }
}
