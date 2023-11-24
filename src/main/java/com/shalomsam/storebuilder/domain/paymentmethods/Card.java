package com.shalomsam.storebuilder.domain.paymentmethods;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Document(collection = "paymentMethods")
public class Card extends PaymentMethod {

    private CardBrandTypes brandName;

    private String cardNumber;

    private CardType cardType;

    private String cardLast4;

    private int expMonth;

    private int expYear;

    private String fingerprint;

    private String country;
}
