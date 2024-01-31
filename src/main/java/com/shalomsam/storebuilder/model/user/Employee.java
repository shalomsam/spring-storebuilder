package com.shalomsam.storebuilder.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "employees")
public class Employee extends User {

    @Transient
    private EmployeeAccess employeeAccess;
    private String employeeAccessId;

    @Transient
    private EmployeeAddress employeeAddress;
    private String employeeAddressId;
}
