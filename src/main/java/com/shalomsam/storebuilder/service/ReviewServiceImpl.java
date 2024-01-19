package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Review;
import com.shalomsam.storebuilder.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReviewServiceImpl implements DomainService<Review> {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }


    @Override
    public Flux<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Review> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Review> create(Review entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Review> updateById(String id, Review entity) {
        return repository.findById(id)
            .map(review -> {

                if (entity.getSku() != null) review.setSku(entity.getSku());
                if (entity.getProduct() != null) review.setProduct(entity.getProduct());
                if (entity.getCustomer() != null) review.setCustomer(entity.getCustomer());
                if (entity.getRating() != null) review.setRating(entity.getRating());
                if (entity.getTitle() != null) review.setTitle(entity.getTitle());
                if (entity.getDescription() != null) review.setDescription(entity.getDescription());

                return review;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Review> updateMany(List<Review> entities) {
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
