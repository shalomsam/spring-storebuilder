package com.shalomsam.storebuilder.domain.user;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Unwrapped;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @MongoId
    private ObjectId id;

    private String unit;

    private String street;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
