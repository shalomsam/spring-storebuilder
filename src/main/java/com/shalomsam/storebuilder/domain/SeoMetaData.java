package com.shalomsam.storebuilder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeoMetaData {
    public String title;

    public String keywords;

    public String description;
}
