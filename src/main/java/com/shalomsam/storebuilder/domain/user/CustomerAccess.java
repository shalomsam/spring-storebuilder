package com.shalomsam.storebuilder.domain.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document
public class CustomerAccess extends Access {
    @DocumentReference
    private Customer customer;
}
