package com.shalomsam.storebuilder.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDto<T> extends ApiResponse {
    private Map<String, T> data;

    @JsonAnySetter
    public SuccessResponseDto<T> addData(String key, T entity) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, entity);
        return this;
    }

    @JsonAnyGetter
    public Map<String, T> getData() {
        return this.data;
    }

    public SuccessResponseDto<T> status(String status) {
        this.setStatus(status);
        return this;
    }
}
