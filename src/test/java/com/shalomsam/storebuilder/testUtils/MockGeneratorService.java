package com.shalomsam.storebuilder.testUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * MockGeneratorService defines the mock generator service class implementation.
 * Each implementing class needs to define 2 `generateMock` methods.
 * One to generate the mock, and the second to write generated mock JSON files to directory.
 *
 * @param <T> Domain model.
 *
 * @author shalomsam
 */

public interface MockGeneratorService<T> {

    Logger log = LoggerFactory.getLogger(MockGeneratorService.class);

    String getCollectionName();

    List<T> generateMock(int size);

    ObjectMapper getObjectMapper();

    default void writeMockToJsonFile(Map<String, List<?>> entityMap) throws IOException {
        for (Map.Entry<String, List<?>> entry: entityMap.entrySet()) {
            String entityName = entry.getKey();
            List<Map> entities = (List<Map>) entry.getValue();

            String stubsDirectoryStr = "src/test/resources/stubs";
            Path directoryPath = Paths.get(stubsDirectoryStr);
            File directory = new File(directoryPath.toString());

            if (!directory.exists()) {
                boolean result = directory.mkdir();
                log.info("mkdir:{} results:{} ", directoryPath, result);
            }

            File file = new File(directory.getPath() + "/" + entityName + ".json");
            String orgJsonList = getObjectMapper().writeValueAsString(entities);

            FileUtils.writeStringToFile(file, orgJsonList, StandardCharsets.UTF_8, false);
        }
    }

    void buildMockRelationShips(ApplicationContext applicationContext);
}
