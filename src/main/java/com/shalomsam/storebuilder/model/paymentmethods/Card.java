package com.shalomsam.storebuilder.model.paymentmethods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {

    private CardBrandTypes brandName;

    private String nameOnCard;

    private String cardNumber;

    private CardType cardType;

    private String cardLast4;

    private int expMonth;

    private int expYear;

    private String fingerprint;

    private String country;
}
