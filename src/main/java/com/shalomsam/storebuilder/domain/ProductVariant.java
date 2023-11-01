package com.shalomsam.storebuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ProductVariant {

    @MongoId
    private ObjectId id;

    @Field("productId")
    @DocumentReference
    private Product product;

    @Field("sellerId")
    @DocumentReference
    private Seller seller;

    private ProductCondition condition;

    private List<ProductAttribute> attributes;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    private int stock;

    @ReadOnlyProperty
    @Field("discountIds")
    @DocumentReference(lazy = true)
    private List<Discount> discounts;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}

