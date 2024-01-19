package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Seller;
import com.shalomsam.storebuilder.repository.SellerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SellerServiceImpl implements DomainService<Seller> {

    private final SellerRepository repository;

    public SellerServiceImpl(SellerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Seller> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Seller> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Seller> create(Seller entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Seller> updateById(String id, Seller entity) {
        return repository.findById(id)
            .map(seller -> {

                if (entity.getOrganization() != null) seller.setOrganization(entity.getOrganization());
                if (entity.getName() != null) seller.setName(entity.getName());
                if (entity.getShopSubDomain() != null) seller.setShopSubDomain(entity.getShopSubDomain());
                if (entity.getSellerType() != null) seller.setSellerType(entity.getSellerType());
                if (entity.getIsOnline() != null) seller.setIsOnline(entity.getIsOnline());
                if (entity.getContactInfo() != null) seller.setContactInfo(entity.getContactInfo());

                return seller;
            })
            .flatMap(repository::save);
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
}
