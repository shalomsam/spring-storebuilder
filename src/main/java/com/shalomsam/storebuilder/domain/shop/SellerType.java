package com.shalomsam.storebuilder.domain.shop;

import lombok.Getter;

@Getter
public enum SellerType {
    // Third-party private sellers
    MARKETPLACE("marketplace"),

    // Owning organization
    ORGANIZATION("organization"),

    // Subset stores of the organization
    STORE("store"),

    // Authorized third-party vendors
    VENDOR("vendor");

    private final String displayName;

    SellerType(String displayName) {
        this.displayName = displayName;
    }

}
