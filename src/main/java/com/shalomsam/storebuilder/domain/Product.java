package com.shalomsam.storebuilder.domain;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    private String sku;

    @Nullable
    private String upc;

    private String title;

    private String description;

    private String brandName;

    @Indexed
    private String modelName;

    @Indexed
    private String category;

    @Field("productVariantId")
    @DocumentReference
    private ProductVariant productVariant;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
