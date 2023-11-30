package com.shalomsam.storebuilder.testUtils;

import java.io.IOException;
import java.util.List;

public interface MockDomainService<T> {

    String getEntityType();

    List<T> generateMock(int size);

    List<T> generateMock(int size, boolean writeMockDataToFile) throws IOException;
}
