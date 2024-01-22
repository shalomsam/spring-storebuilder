package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Offer;
import com.shalomsam.storebuilder.domain.shop.OfferStatus;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.domain.shop.Seller;
import com.shalomsam.storebuilder.repository.OfferRepository;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import com.shalomsam.storebuilder.repository.SellerRepository;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferMockGeneratorService implements MockGeneratorService<Offer> {

    public static String COLLECTION_NAME = "offers";

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
    public List<Offer> generateMock(int size) {
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Offer offer = Offer.builder()
                .id(new ObjectId().toString())
                .offerStatus(faker.options().option(OfferStatus.class))
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            offers.add(offer);
        }

        return offers;
    }


    @Override
    public void buildMockRelationShips(ApplicationContext applicationContext) {
        OfferRepository offerRepository = applicationContext.getBean(OfferRepository.class);
        ProductVariantRepository variantRepository = applicationContext.getBean(ProductVariantRepository.class);
        SellerRepository sellerRepository = applicationContext.getBean(SellerRepository.class);

        Flux<Offer> offerFlux = offerRepository.findAll();
        List<ProductVariant> productVariants = variantRepository.findAll().toStream().toList();
        List<Seller> sellers = sellerRepository.findAll().toStream().toList();

        offerFlux.map(offer -> {
            ProductVariant randomVariant = faker.options().nextElement(productVariants);
            Seller randomSeller = faker.options().nextElement(sellers);

            offer.setProductVariant(randomVariant);
            offer.setSeller(randomSeller);

            randomVariant.getOffers().add(offer);
            variantRepository.save(randomVariant);

            randomSeller.getOffers().add(offer);
            sellerRepository.save(randomSeller);

            return offer;
        })
        .flatMap(offerRepository::save)
        .flatMap((offer) -> {
            productVariants.forEach(productVariant -> {
                productVariant.setSku(MockHelper.generateMockSku(productVariant));
                variantRepository.save(productVariant);
            });

            return null;
        });
    }
}
