package com.shalomsam.storebuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrganizationServiceImpl implements DomainService<Organization> {

    private final OrganizationRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    OrganizationServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Organization> getAll() {
        // TODO: implement pageable defaults
        return repository.findAll();
    }

    @Override
    public Mono<Organization> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Organization> create(Organization organization) {
        return repository.save(organization);
    }

    @Override
    public Mono<Organization> updateById(String id, Organization orgDTO) {
        return repository.findById(id).flatMap(organization -> repository.save(orgDTO));
    }

    @Override
    public Flux<Organization> updateMany(List<Organization> orgDTOs) {
        return repository.saveAll(orgDTOs);
    }

    @Override
    public Mono<Organization> deleteById(String id) {
        return null;
    }

    //TODO: Add bulk update method
}
