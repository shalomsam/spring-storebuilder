package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.shop.Seller;
import com.shalomsam.storebuilder.domain.shop.SellerType;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerMockGeneratorService implements MockGeneratorService<Seller> {

    public static String COLLECTION_NAME = "sellers";

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    @Override
    public List<Seller> generateMock(int size) {
        List<Seller> sellers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Address> addresses = new ArrayList<>();
            addresses.add(MockHelper.generateMockAddress());
            Seller seller = Seller.builder()
                .id(new ObjectId().toString())
                .name(faker.expression("Seller: #{commerce.vendor}"))
                .shopSubDomain(faker.internet().domainWord())
                .sellerType(faker.options().option(SellerType.class))
                .isOnline(faker.bool().bool())
                .contactInfo(
                    ContactInfo.builder()
                        .email(faker.internet().emailAddress())
                        .chatLink(faker.internet().url())
                        .phone(faker.phoneNumber().phoneNumber())
                        .addresses(addresses)
                        .build()
                )
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            sellers.add(seller);
        }

        return sellers;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Flux<Seller> sellerFlux = mongoTemplate.findAll(Seller.class);
        Mono<List<Organization>> organizationsMono = mongoTemplate.findAll(Organization.class).collectList();

        organizationsMono.flatMapMany(organizations -> {
            return sellerFlux.map(seller -> {
                Organization randomOrg = faker.options().nextElement(organizations);
                seller.setOrganizationId(randomOrg.getId());
                return seller;
            })
            .flatMap(mongoTemplate::save);
        })
        .blockFirst();
    }
}
