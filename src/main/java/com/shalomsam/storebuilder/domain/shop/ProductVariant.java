package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "productVariants")
public class ProductVariant {

    @MongoId
    private String id;

    @Field("productId")
    @DocumentReference
    private Product product;

    @Indexed(unique = true)
    private String sku;

    @Indexed(unique = true)
    private String upc;

    @Field("sellerId")
    @DocumentReference
    private Seller seller;

    private ProductCondition condition;

    private List<ProductAttribute> attributes;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal listPrice;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal salePrice;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal bulkPrice;

    @Field("inventoryIds")
    @DocumentReference(lazy = true)
    private List<Inventory> inventoryList;

    @ReadOnlyProperty
    @Field("discountIds")
    @DocumentReference(lazy = true)
    private List<Discount> discounts;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}

