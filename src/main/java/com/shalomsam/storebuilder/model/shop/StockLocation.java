package com.shalomsam.storebuilder.model.shop;

import com.shalomsam.storebuilder.model.user.PersistableAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "stockLocations")
public class StockLocation extends PersistableAddress {
}
