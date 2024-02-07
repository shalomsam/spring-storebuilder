package com.shalomsam.storebuilder.model.paymentmethods;

import lombok.Getter;

@Getter
public enum PaymentMethodType {
    CARD("card"),
    STRIPE("stripe"),
    CASH("cash");

    private final String displayName;

    PaymentMethodType(String displayName) {
        this.displayName = displayName;
    }

}
