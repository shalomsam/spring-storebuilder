package com.shalomsam.storebuilder.model.paymentmethods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeDetails {
    private String accountName;
    private String apiKey;
    private String successUrlPath;
    private String failureUrlPath;
}
