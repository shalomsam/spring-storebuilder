package com.shalomsam.storebuilder.domain.shop;

import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

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

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private String brandName;

    @Indexed
    private String modelName;

    @Indexed
    private String category;

    @Field("productVariantIds")
    @DocumentReference
    private List<ProductVariant> productVariants;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
