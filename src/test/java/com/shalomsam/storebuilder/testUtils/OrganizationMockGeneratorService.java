package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrganizationMockGeneratorService implements MockGeneratorService<Organization> {

    static String COLLECTION_NAME = "organizations";

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<Organization> generateMock(int size) {
        List<Organization> organizations = new ArrayList<>();
        Faker faker = new Faker();

        for(int i = 0; i < size; i++) {
            String companyName = faker.company().name();
            List<Address> companyLocations = new ArrayList<>();

            // generate company address list
            companyLocations.add(
                MockHelper.generateMockAddress()
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
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            organizations.add(organization);
        }
        return organizations;
    }

    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {

    }
}
