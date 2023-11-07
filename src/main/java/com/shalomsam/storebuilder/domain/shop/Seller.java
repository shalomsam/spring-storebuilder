package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.Organization;
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
public class Seller {
    @MongoId
    private ObjectId id;

    @Field("organizationId")
    @DocumentReference
    private Organization organization;

    private String name;

    private String shopSubDomain;

    private SellerType sellerType;

    private boolean isOnline;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
