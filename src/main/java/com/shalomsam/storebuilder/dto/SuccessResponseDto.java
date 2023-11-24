package com.shalomsam.storebuilder.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SuccessResponseDto<T> extends ApiResponse {
    private Map<String, T> data;

    public static abstract class SuccessResponseDtoBuilder<T, C extends SuccessResponseDto<T>, B extends SuccessResponseDtoBuilder<T, C, B>> extends ApiResponseBuilder<C,B> {
        public <V> B data(String key, V data) {
            if (this.data == null) {
                this.data = new HashMap<>();
            }
            //noinspection unchecked
            this.data.put(key, (T) data);
            return self();
        }
    }
}
