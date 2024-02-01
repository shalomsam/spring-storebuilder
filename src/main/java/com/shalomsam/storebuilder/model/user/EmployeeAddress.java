package com.shalomsam.storebuilder.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "employeeAddresses")
public class EmployeeAddress extends PersistableAddress {}
