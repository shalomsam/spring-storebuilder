package com.shalomsam.storebuilder.domain.shop;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceSummary {
    private String title;
    private String description;

    @Nullable
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal value;

    @Nullable
    @Field(targetType = FieldType.DOUBLE)
    private Double percentage;
}
