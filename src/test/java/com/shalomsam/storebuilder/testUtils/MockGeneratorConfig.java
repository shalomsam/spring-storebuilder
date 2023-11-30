package com.shalomsam.storebuilder.testUtils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mocks")
public class MockGeneratorConfig {
    private String stubsDirectoryPath;
    private boolean shouldWriteMockDataToFile;
}
