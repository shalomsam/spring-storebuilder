package com.shalomsam.storebuilder.domain.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pricing {
    @Transient
    private Currency currency;
    private String currencyCode;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        this.setCurrency(Currency.getInstance(currencyCode));
    }
}
