package com.shalomsam.storebuilder.domain.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document
public class EmployeeAccess extends Access {

    @Field("employeeAccessId")
    @DocumentReference
    private EmployeeAccess employeeAccess;

    @Field("employeeAddressIds")
    @DocumentReference
    private List<EmployeeAddress> employeeAddresses;

}
