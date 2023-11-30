package com.shalomsam.storebuilder;

import com.shalomsam.storebuilder.testUtils.MockDomainService;
import com.shalomsam.storebuilder.testUtils.MockGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@TestComponent
public class MockDataGenerator {

    static String[] collectionNames = new String[]{
            "organizations",
            "cards",
            "carts",
            "discounts",
            "inventories",
            "orders",
            "products",
            "productVariants",
            "reviews",
            "sellers",
            "stockLocations",
            "transactions",
            "customers",
            "customerAccess",
            "customerAddress",
            "employees",
            "employeeAccess",
            "employeeAddress"
    };

    private final List<MockDomainService<?>> domainServices;

    private final ResourceLoader resourceLoader;

    public final MockGeneratorConfig mockGeneratorConfig;

    private final Map<String, List<String>> entityIds = new HashMap<>();

    public MockDataGenerator(
            List<MockDomainService<?>> domainServices,
            ResourceLoader resourceLoader,
            MockGeneratorConfig config
    ) {
        this.domainServices = domainServices;
        this.resourceLoader = resourceLoader;
        this.mockGeneratorConfig = config;
    }

    public void generateMockData(int size) {
        log.info("Generating mock data sets of size: {} ", size);

        // check if json files exist in stubs folder
        Resource resource = resourceLoader.getResource(mockGeneratorConfig.getStubsDirectoryPath() + "/*.json");

        if (!resource.exists()) {
            // get all domain classes
            domainServices.forEach(domainService -> {
                try {
                    List<String> ids = domainService.generateMock(size, mockGeneratorConfig.isShouldWriteMockDataToFile())
                            .stream()
                            .map(MockDataGenerator::extractId)
                            .toList();

                    entityIds.put(domainService.getEntityType(), ids);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static String extractId(Object object) {
        try {
            Field field = object.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return String.valueOf(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
