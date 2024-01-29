package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.shop.Product;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.domain.shop.Seller;
import com.shalomsam.storebuilder.domain.shop.StockLocation;
import com.shalomsam.storebuilder.domain.user.Address;
import net.datafaker.Faker;
import org.bson.types.ObjectId;

import java.text.Normalizer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class MockHelper {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase();
    }

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

    public static StockLocation generateMockStockLocation() {
        Faker faker = new Faker();
        String state = faker.address().stateAbbr();

        return StockLocation.builder()
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

    public static String generateMockSku(ProductVariant productVariant, Product product, Seller seller) {
        return String.format(
            "%.5s-%.5s-%.5s",
            product.getBrandName().toUpperCase(),
            productVariant.getAttributes().stream().map(productAttribute -> String.valueOf(productAttribute.getName().charAt(0) + productAttribute.getValue().charAt(0))).collect(Collectors.joining("")),
            seller.getId()
        );
    }
}
