package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.Organization;
import com.shalomsam.storebuilder.model.shop.Seller;
import com.shalomsam.storebuilder.repository.SellerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class SellerServiceImpl implements DomainService<Seller> {

    private final SellerRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public SellerServiceImpl(SellerRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Seller> getAll() {
        return repository.findAll()
            .flatMap(this::zipWithRelatedEntities)
            .onErrorResume(e -> {
                log.error("Error processing sellers: ", e);
                return Flux.empty();
            });
    }

    @Override
    public Mono<Seller> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Seller> create(Seller entity) {
        return repository.save(entity)
            .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Seller> updateById(String id, Seller entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getOrganizationId() != null) update.set("organizationId", entity.getOrganizationId());
        if (entity.getName() != null) update.set("name", entity.getName());
        if (entity.getShopSubDomain() != null) update.set("shopSubDomain", entity.getShopSubDomain());
        if (entity.getSellerType() != null) update.set("sellerType", entity.getSellerType());
        if (entity.getIsOnline() != null) update.set("isOnline", entity.getIsOnline());
        if (entity.getContactInfo() != null) update.set("contactInfo", entity.getContactInfo());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Seller.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Seller> updateMany(List<Seller> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return repository.deleteById(id).thenReturn(1);
    }

    @Override
    public Mono<Integer> deleteManyById(List<String> ids) {
        return repository.deleteAllByIdIn(ids);
    }

    @Override
    public Mono<Long> getCount() {
        return repository.count();
    }

    private Mono<Seller> zipWithRelatedEntities(Seller seller) {
        Mono<Organization> orgMono = mongoTemplate.findById(seller.getOrganizationId(), Organization.class).defaultIfEmpty(Organization.builder().build());

        return orgMono.map(organization -> {
            seller.setOrganization(organization);
            return seller;
        }).onErrorResume(e -> {
            log.error("Error enriching seller: {}, orgId: {} ", seller.getId(), seller.getOrganizationId(), e);
            return Mono.just(seller);
        });
    }
}
