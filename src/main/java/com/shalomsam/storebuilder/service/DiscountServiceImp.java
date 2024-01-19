package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Discount;
import com.shalomsam.storebuilder.repository.DiscountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class DiscountServiceImp implements DomainService<Discount> {

    private final DiscountRepository repository;

    public DiscountServiceImp(DiscountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Discount> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Discount> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Discount> create(Discount entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Discount> updateById(String id, Discount entity) {
        return repository.findById(id)
            .map(discount -> {

                if (entity.getProductVariant() != null) discount.setProductVariant(entity.getProductVariant());
                if (entity.getTitle() != null) discount.setTitle(entity.getTitle());
                if (entity.getDescription() != null) discount.setDescription(entity.getDescription());
                if (entity.getAmount() != null) discount.setAmount(entity.getAmount());
                if (entity.getPercentage() != null) discount.setPercentage(entity.getPercentage());
                if (entity.getStartDateTime() != null) discount.setStartDateTime(entity.getStartDateTime());
                if (entity.getEndDateTime() != null) discount.setEndDateTime(entity.getEndDateTime());

                return discount;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Discount> updateMany(List<Discount> entities) {
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
