package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventories")
public class Inventory {
    @MongoId
    private String id;

    @Field("productVariantId")
    @DocumentReference
    private ProductVariant productVariant;

    private StockLocation location;

    private Integer stockCount;

    @ReadOnlyProperty
    @Field("orderIds")
    @DocumentReference(lookup = "{'inventory': ?#{#self.id} }")
    private List<Order> orders;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
