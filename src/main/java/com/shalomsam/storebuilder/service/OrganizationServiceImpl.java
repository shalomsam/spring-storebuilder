package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class OrganizationServiceImpl implements DomainService<Organization> {

    private final OrganizationRepository repository;

    public OrganizationServiceImpl(OrganizationRepository repository) {
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
        return repository.findById(id)
                .map(o -> {
                    if (orgDTO.getAuditMetadata() != null) o.setAuditMetadata(orgDTO.getAuditMetadata());
                    if (orgDTO.getName() != null) o.setName(orgDTO.getName());
                    if (orgDTO.getShopUrl() != null) o.setShopUrl(orgDTO.getShopUrl());
                    if (orgDTO.getContactInfo() != null) o.setContactInfo(orgDTO.getContactInfo());
                    return o;
                })
                .flatMap(repository::save);
    }

    @Override
    public Flux<Organization> updateMany(List<Organization> orgDTOs) {
        return repository.saveAll(orgDTOs);
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

    //TODO: Add bulk update method
}
