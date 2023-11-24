package com.shalomsam.storebuilder.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder(builderMethodName = "baseBuilder")
public class ApiResponse {
    @Getter
    public static enum ApiResponseType {
        SUCCESS("success"),
        ERROR("error");

        private final String value;

        ApiResponseType(String value) {
            this.value = value;
        }
    }

    private final String status;
}
