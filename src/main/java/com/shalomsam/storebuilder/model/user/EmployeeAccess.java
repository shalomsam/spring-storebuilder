package com.shalomsam.storebuilder.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

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
