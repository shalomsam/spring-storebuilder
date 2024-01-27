package com.shalomsam.storebuilder.domain.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "employeeAccesses")
public class EmployeeAccess extends Access {

    @Transient
    private EmployeeAccess employeeAccess;

    private String employeeAccessId;

    @Transient
    private List<EmployeeAddress> employeeAddresses;

}
