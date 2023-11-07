package com.shalomsam.storebuilder.domain.paymentmethods;

import lombok.Getter;

@Getter
public enum PaymentMethodType {
    CARD("card"),
    BANK_TRANSFER("bankTransfer"),
    STRIPE("stripe");

    private final String displayName;

    PaymentMethodType(String displayName) {
        this.displayName = displayName;
    }

}
