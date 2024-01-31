package com.shalomsam.storebuilder.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "customerAddresses")
public class CustomerAddress extends Address {}
