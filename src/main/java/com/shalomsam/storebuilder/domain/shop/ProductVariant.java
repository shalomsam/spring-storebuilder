package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Field("productId")
    @DocumentReference(lazy = true, collection = "products")
    private Product product;

    @Indexed(unique = true)
    private String sku;

    @Indexed(unique = true)
    private String upc;

    @Field("sellerId")
    @DocumentReference(collection = "sellers")
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

    @Field("offerIds")
    @DocumentReference
    private List<Offer> offers;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}

