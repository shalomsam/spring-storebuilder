package com.shalomsam.storebuilder.domain.shop;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private String sku;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal skuPrice;

    private int quantity;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;

    @NonNull
    private String currencyCode = Currency.getInstance(Locale.CANADA).getCurrencyCode();

}
