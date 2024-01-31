package com.shalomsam.storebuilder.model.shop;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private String sku;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal skuPrice;

    @NonNull
    private Integer quantity;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;

    @Builder.Default
    private String currencyCode = "CAD";
}
