package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.*;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import com.shalomsam.storebuilder.service.DomainService;
import com.shalomsam.storebuilder.service.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductMockGeneratorService implements MockGeneratorService<Product> {

    static String COLLECTION_NAME = "products";

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<Product> generateMock(int size) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String brand = faker.commerce().brand();
            String modelName = faker.commerce().productName();

            Product product = Product.builder()
                .id(new ObjectId().toString())
                .brandName(brand)
                .modelName(modelName)
                .title(String.format("%s - %s", brand, modelName))
                .description(faker.lorem().characters(200))
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            products.add(product);
        }

        return products;
    }

    public List<Seller> generateMockSellers(int size) {
        List<Seller> sellers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Address> mockAddresses = new ArrayList<>();
            mockAddresses.add(MockHelper.generateMockAddress());
            sellers.add(
                Seller.builder()
                    .id(new ObjectId().toString())
                    .sellerType(faker.options().option(SellerType.class))
                    .isOnline(faker.bool().bool())
                    .shopSubDomain(faker.internet().domainWord())
                    .name(faker.expression("Seller: #{company.name}"))
                    .auditMetadata(MockHelper.generateMockAuditMetadata())
                    .contactInfo(
                        ContactInfo.builder()
                            .addresses(mockAddresses)
                            .email(faker.internet().emailAddress())
                            .chatLink(faker.internet().url())
                            .build()
                    )
                    .build()
            );
        }
        return sellers;
    }



    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        List<Map> products = (List<Map>) entityMap.get(COLLECTION_NAME);
        List<Map> categories = (List<Map>) entityMap.get("category");
        List<Map> productVariants = (List<Map>) entityMap.get("productVariant");
        int relationSize = 3;

        products = products.stream().peek(product -> {
            List<String> mockRelatedVariants = new ArrayList<>();
            List<String> mockRelatedCategories = new ArrayList<>();

            for (int i = 0; i < relationSize; i++) {
                Map relatedMockCategory = faker.options().nextElement(categories);
                Map relatedMockProdVar = faker.options().nextElement(productVariants);

                relatedMockProdVar.put("productId", product.get("id"));
                mockRelatedCategories.add(relatedMockCategory.get("id").toString());
                mockRelatedVariants.add(relatedMockProdVar.get("id").toString());
            }

            product.put("categoryIds", mockRelatedCategories);
            product.put("productVariantIds", mockRelatedVariants);
        }).toList();

        entityMap.put(getCollectionName(), products);
    }

    public void buildMockRelationShips(ApplicationContext applicationContext) {
        DomainService<Product> productDomainService = applicationContext.getBean(ProductServiceImpl.class);

        Flux<Product> productFlux = productDomainService.getAll();
    }
}
