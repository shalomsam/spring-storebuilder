package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.user.Address;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document
public class StockLocation extends Address {
}
