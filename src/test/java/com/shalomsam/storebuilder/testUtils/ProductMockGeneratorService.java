package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Product;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.repository.ProductRepository;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductMockGeneratorService implements MockGeneratorService<Product> {

    static String COLLECTION_NAME = "products";

    @Autowired
    public ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Autowired
    private ProductVariantMockGenerator productVariantMockGenerator;

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }


    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
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

            // Note: the generated variant mocks won't be linked until relationship building phase
            // this is to avoid infinite recursion in Jackson (as we write to file)
            // we also cannot set ProductVariant like so (below) coz that wld embed the document
            // `product.setProductVariants(productVariantMockGenerator.generateMock(2, product));`
            // which we don't want, hence we'll call the `productVariantMockGenerator` to generate
            // the mapping, and use that during `buildMockRelationShips`
            productVariantMockGenerator.generateMock(2, product);
            products.add(product);
        }

        return products;
    }

    public void buildMockRelationShips(ApplicationContext applicationContext) {
        Map<String, List<String>> productToVariantMap = productVariantMockGenerator.getProductToVariantMap();
        ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
        ProductVariantRepository variantRepository = applicationContext.getBean(ProductVariantRepository.class);

        productToVariantMap.forEach((pId,vIds) -> {
            Product product = productRepository.findById(pId).block();
            List<ProductVariant> variants = variantRepository.findAllById(vIds).collectList().block();

            assert product != null;
            List<ProductVariant> productVariants = new ArrayList<>(product.getProductVariants());
            productVariants.addAll(variants);
            product.setProductVariants(productVariants);
            productRepository.save(product);

            variants.forEach(variant -> {
                variant.setProduct(product);
                variantRepository.save(variant);
            });
        });
    }
}
