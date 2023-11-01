package com.shalomsam.storebuilder.domain.paymentmethods;

import lombok.Getter;

@Getter
public enum PaymentMethodType {
    DEBIT_CARD("debitCard"),
    CREDIT_CARD("creditCard"),
    BANK_TRANSFER("bankTransfer"),
    STRIPE("stripe");

    private final String displayName;

    PaymentMethodType(String displayName) {
        this.displayName = displayName;
    }

}
