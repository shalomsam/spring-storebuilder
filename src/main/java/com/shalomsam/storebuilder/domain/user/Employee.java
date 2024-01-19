package com.shalomsam.storebuilder.domain.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "employees")
public class Employee extends User {
    @Field("employeeAccessId")
    @DocumentReference
    private EmployeeAccess employeeAccess;

    @Field("employeeAddressId")
    @DocumentReference
    private EmployeeAddress employeeAddress;
}
