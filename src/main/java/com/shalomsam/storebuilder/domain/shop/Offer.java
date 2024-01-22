package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "offers")
public class Offer {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private OfferStatus offerStatus;

    @Field(name = "sellerId")
    @DocumentReference(lazy = true, collection = "sellers")
    private Seller seller;

    @Field(name = "productVariantId")
    @DocumentReference(lazy = true, collection = "productVariants")
    private ProductVariant productVariant;

    @Field(name = "inventoryId")
    @DocumentReference(lazy = true, collection = "inventories")
    private Inventory inventory;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
