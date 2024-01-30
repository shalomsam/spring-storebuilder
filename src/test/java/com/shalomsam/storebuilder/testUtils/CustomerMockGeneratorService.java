package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.domain.user.CustomerAccess;
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
public class CustomerMockGeneratorService implements MockGeneratorService<Customer> {

    static String COLLECTION_NAME = "customers";

    private final Faker faker = new Faker();

    @Autowired
    public ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public ObjectMapper objectMapper;

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<Customer> generateMock(int size) {
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Customer customer = Customer.builder()
                .id(new ObjectId().toString())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();

            customer.setCustomerAccessId(generateCustomerAccess(customer));
            customers.add(customer);
        }

        return customers;
    }

    private String generateCustomerAccess(Customer customer) {
        String accessId = new ObjectId().toString();
        CustomerAccess access = CustomerAccess.builder()
            .id(accessId)
            .customerId(customer.getId())
            .passHash(faker.hashing().sha256())
            .build();

        mongoTemplate.save(access).block();

        return accessId;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Mono<List<Organization>> organizationsMono = mongoTemplate.findAll(Organization.class).collectList();
        Flux<Customer> customerFlux = mongoTemplate.findAll(Customer.class);

        organizationsMono.flatMapMany(organizations -> customerFlux.map(customer -> {
            Organization organization = faker.options().nextElement(organizations);
            customer.setOrganizationId(organization.getId());
            return customer;
        })
        .flatMap(mongoTemplate::save))
        .blockFirst();
    }
}
