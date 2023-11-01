package com.shalomsam.storebuilder.domain.user;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document
public class EmployeeAddress extends Address {
}
