package com.shalomsam.storebuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto extends ApiResponse {
    private String message;

    public ErrorResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseDto status(String value) {
        this.setStatus(value);
        return this;
    }
}
