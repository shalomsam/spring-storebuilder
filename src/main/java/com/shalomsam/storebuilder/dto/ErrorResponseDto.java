package com.shalomsam.storebuilder.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ErrorResponseDto extends ApiResponse {
    private String message;
}
