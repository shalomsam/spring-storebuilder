package com.shalomsam.storebuilder;

import com.shalomsam.storebuilder.testUtils.MockGeneratorService;
import com.shalomsam.storebuilder.testUtils.MockGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@TestComponent
public class MockDataGenerator {
    private final List<MockGeneratorService<?>> mockGeneratorServices;

    private final ResourceLoader resourceLoader;

    public final MockGeneratorConfig mockGeneratorConfig;

    private final Map<String, List<?>> entityMap = new HashMap<>();

    public MockDataGenerator(
            List<MockGeneratorService<?>> mockGeneratorServices,
            ResourceLoader resourceLoader,
            MockGeneratorConfig config
    ) {
        this.mockGeneratorServices = mockGeneratorServices;
        this.resourceLoader = resourceLoader;
        this.mockGeneratorConfig = config;
    }

    public void generateMockData(int size) {
        log.info("Generating mock data sets of size: {} ", size);

        // check if json files exist in stubs folder
        Resource resource = resourceLoader.getResource(mockGeneratorConfig.getStubsDirectoryPath() + "/*.json");

        mockGeneratorServices.forEach(domainService -> {
            try {
                List<?> entities = domainService.generateMock(size);

                entityMap.put(domainService.getCollectionName(), entities);

                if (!resource.exists() && mockGeneratorConfig.isShouldWriteMockDataToFile()) {
                    // get all domain classes
                    domainService.writeMockToJsonFile(entityMap);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


    }
}
