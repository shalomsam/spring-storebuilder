package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "productVariants")
public class ProductVariant extends BaseDocument {

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
    @Builder.Default
    private Currency currency = Currency.getInstance(Locale.CANADA);
    @Builder.Default
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
}

