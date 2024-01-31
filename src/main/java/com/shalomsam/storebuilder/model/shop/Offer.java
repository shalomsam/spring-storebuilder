package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "offers")
public class Offer extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private OfferStatus offerStatus;

    @Transient
    private Seller seller;
    private String sellerId;

    @Transient
    private ProductVariant productVariant;
    private String productVariantId;

    @Transient
    private Inventory inventory;
    private String inventoryId;
}
