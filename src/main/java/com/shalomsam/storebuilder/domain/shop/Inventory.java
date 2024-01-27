package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventories")
public class Inventory {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String productVariantId;
    @Transient
    private ProductVariant productVariant;

    private String stockLocationId;
    @Transient
    private StockLocation stockLocation;

    private Integer stockCount;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
