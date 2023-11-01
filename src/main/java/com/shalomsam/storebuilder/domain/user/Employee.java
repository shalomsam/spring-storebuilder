package com.shalomsam.storebuilder.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document
public class Employee extends User {
    @Field("employeeAccessId")
    @DocumentReference
    private EmployeeAccess employeeAccess;

    @Field("employeeAddressId")
    @DocumentReference
    private EmployeeAddress employeeAddress;
}
