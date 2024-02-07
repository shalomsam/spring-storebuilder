package com.shalomsam.storebuilder.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

    private String unit;

    private String buildingNumber;

    private String street;

    private String city;

    private String state;

    private String country;

    private String postalCode;
}
