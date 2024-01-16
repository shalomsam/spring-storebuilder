package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.Organization;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrganizationRepository extends ReactiveMongoRepository<Organization, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
