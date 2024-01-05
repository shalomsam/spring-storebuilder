package com.shalomsam.storebuilder.testUtils;

import java.io.IOException;
import java.util.List;

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

    String getEntityType();

    List<T> generateMock(int size);

    List<T> generateMock(int size, boolean writeMockDataToFile) throws IOException;
}
