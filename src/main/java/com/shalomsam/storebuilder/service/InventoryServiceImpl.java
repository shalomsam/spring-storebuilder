package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Inventory;
import com.shalomsam.storebuilder.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl implements DomainService<Inventory> {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Inventory> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Inventory> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Inventory> create(Inventory entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Inventory> updateById(String id, Inventory entity) {
        return repository.findById(id)
            .map(inventory -> {
                if (entity.getProductVariant() != null) inventory.setProductVariant(entity.getProductVariant());
                if (entity.getLocation() != null) inventory.setLocation(entity.getLocation());
                if (entity.getStockCount() != null) inventory.setStockCount(entity.getStockCount());
                if (entity.getOrders() != null) inventory.getOrders().addAll(entity.getOrders());

                return inventory;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Inventory> updateMany(List<Inventory> entities) {
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
