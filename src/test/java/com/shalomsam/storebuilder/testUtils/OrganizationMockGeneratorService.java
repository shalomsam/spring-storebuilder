package com.shalomsam.storebuilder.testUtils;

import com.github.javafaker.Faker;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrganizationMockGeneratorService implements MockGeneratorService<Organization> {

    private final ObjectMapper objectMapper;

    public OrganizationMockGeneratorService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getEntityType() {
        return "organization";
    }

    @Override
    public List<Organization> generateMock(int size) {
        List<Organization> organizations = new ArrayList<>();
        Faker faker = new Faker();

        for(int i = 0; i < size; i++) {
            String companyName = faker.company().name();
            List<Address> companyLocations = new ArrayList<>();
            String state = faker.address().stateAbbr();

            // create mock auditMeta
            ZonedDateTime createdAt = ZonedDateTime.of(
                faker
                    .date()
                    .past(2, TimeUnit.DAYS)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
                ZoneId.systemDefault()
            );
            ZonedDateTime updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
            AuditMetadata auditMetadata = new AuditMetadata();
            auditMetadata.setUpdatedAt(updatedAt);
            auditMetadata.setCreatedAt(createdAt);

            // generate company address list
            companyLocations.add(
                Address.builder()
                    .id(new ObjectId().toString())
                    .city(faker.address().city())
                    .unit(String.valueOf(faker.number().randomNumber()))
                    .buildingNumber(faker.address().buildingNumber())
                    .street(faker.address().streetName())
                    .city(faker.address().city())
                    .state(state)
                    .country(faker.address().country())
                    .postalCode(faker.address().zipCodeByState(state))
                    .build()
            );

            Organization organization = Organization.builder()
                .id(new ObjectId().toString())
                .name(companyName)
                .shopUrl(faker.company().url())
                .contactInfo(
                    ContactInfo.builder()
                        .email(faker.internet().emailAddress(companyName.toLowerCase()))
                        .phone(faker.phoneNumber().phoneNumber())
                        .chatLink(faker.internet().url() + "/chat-link")
                        .addresses(companyLocations)
                        .build()
                )
                .auditMetadata(auditMetadata)
                .build();

            organizations.add(organization);
        }
        return organizations;
    }

    @Override
    public List<Organization> generateMock(int size, boolean writeMockDataToFile) throws IOException {

        List<Organization> organizations = generateMock(size);

        if (writeMockDataToFile) {
            String stubsDirectoryStr = "src/test/resources/stubs";
            Path directoryPath = Paths.get(stubsDirectoryStr);
            File directory = new File(directoryPath.toString());

            if (!directory.exists()) {
                boolean result = directory.mkdir();
                log.info("mkdir:{} results:{} ", directoryPath, result);
            }

            File file = new File(directory.getPath() + "/organizations.json");
            String orgJsonList = objectMapper.writeValueAsString(organizations);

            FileUtils.writeStringToFile(file, orgJsonList, StandardCharsets.UTF_8, false);
        }

        return organizations;
    }
}
