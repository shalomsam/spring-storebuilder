package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.user.Address;
import net.datafaker.Faker;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public final class MockHelper {

    public static AuditMetadata generateMockAuditMetadata() {
        Faker faker = new Faker();
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

        return auditMetadata;
    }

    public static Address generateMockAddress() {
        Faker faker = new Faker();
        String state = faker.address().stateAbbr();

        return Address.builder()
            .id(new ObjectId().toString())
            .city(faker.address().city())
            .unit(String.valueOf(faker.number().randomNumber()))
            .buildingNumber(faker.address().buildingNumber())
            .street(faker.address().streetName())
            .city(faker.address().city())
            .state(state)
            .country(faker.address().country())
            .postalCode(faker.address().zipCodeByState(state))
            .build();
    }
}
