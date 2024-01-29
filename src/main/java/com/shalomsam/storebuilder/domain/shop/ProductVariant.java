package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "productVariants")
public class ProductVariant {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Transient
    private Product product;
    private String productId;

    @Indexed(unique = true)
    private String sku;

    @Indexed(unique = true)
    private String upc;

    @Transient
    private Seller seller;
    private String sellerId;

    private ProductCondition condition;

    private List<ProductAttribute> attributes;

    @Transient
    private Currency currency = Currency.getInstance(Locale.CANADA);
    private String currencyCode = "CAD";

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal listPrice;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal salePrice;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal bulkPrice;

    @Transient
    private List<Inventory> inventoryList;

    @Transient
    private List<Discount> discounts;

    @Transient
    private List<Offer> offers;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}

