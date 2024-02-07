package com.shalomsam.storebuilder.service;

import lombok.Getter;

@Getter
public enum UpdateType {
    PUSH("push"),
    SET("set"),
    POP("pop");

    private final String displayName;

    UpdateType(String name) {
        this.displayName = name;
    }
}
