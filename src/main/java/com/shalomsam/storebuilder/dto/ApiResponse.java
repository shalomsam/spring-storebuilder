package com.shalomsam.storebuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    @Getter
    public enum ApiResponseType {
        SUCCESS("success"),
        ERROR("error");

        private final String value;

        ApiResponseType(String value) {
            this.value = value;
        }
    }

    private String status;

}
