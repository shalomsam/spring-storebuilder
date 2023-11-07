package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Inventory {
    @MongoId
    private ObjectId id;

    @Field("productVariantId")
    @DocumentReference
    private ProductVariant productVariant;

    private StockLocation location;

    private int count;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
