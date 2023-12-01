package com.shalomsam.storebuilder;

import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.repository.OrganizationRepository;
import com.shalomsam.storebuilder.testUtils.MockDomainService;
import com.shalomsam.storebuilder.testUtils.MockGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import org.testcontainers.containers.Container.ExecResult;

import java.io.IOException;
import java.util.List;

@Slf4j
@DataMongoTest
@ComponentScan(basePackages = "com.shalomsam.storebuilder.testUtils")
@EnableConfigurationProperties(MockGeneratorConfig.class)
@Testcontainers
public class BaseRepositoryTest {

    public static int MONGO_PORT = 27017;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Container
    private static final GenericContainer<?> mongoDbContainer = new GenericContainer<>(DockerImageName.parse("mongo:latest"))
            .withAccessToHost(true)
            .withExposedPorts(MONGO_PORT)
            .withEnv("MONGO_INIT_DATABASE", "storebuilder-test")
            .withFileSystemBind(
                    MountableFile.forClasspathResource("/stubs/").getResolvedPath(),
                    "/stubs/",
                    BindMode.READ_WRITE
            )
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("mongoInit.sh"),
                    "/"
            );

    @BeforeAll
    public static void setUp(
            @Autowired List<MockDomainService<?>> domainServices,
            @Autowired ResourceLoader resourceLoader,
            @Autowired MockGeneratorConfig mockGeneratorConfig
    ) throws IOException, InterruptedException {

        // Generate Mock data if not exists
        MockDataGenerator mockDataGenerator = new MockDataGenerator(domainServices, resourceLoader, mockGeneratorConfig);
        mockDataGenerator.generateMockData(10);

        mongoDbContainer.setPortBindings(List.of(MONGO_PORT + ":" + MONGO_PORT));
        mongoDbContainer.start();

        ExecResult result = mongoDbContainer.execInContainer("bash", "/mongoInit.sh");
        log.info("Exec stdout:\n {}", result.getStdout());
        log.info("Exec stderr:\n {}", result.getStderr());
        log.info("exit code={}", result.getExitCode());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDbContainer::getHost);
        registry.add("spring.data.mongodb.port", () -> mongoDbContainer.getMappedPort(MONGO_PORT));
        registry.add("spring.data.mongodb.database", () -> "storebuilder-test");
        registry.add("spring.data.mongodb.uri", () -> String.format("mongodb://%s:%s/%s", mongoDbContainer.getHost(), mongoDbContainer.getMappedPort(MONGO_PORT), "storebuilder-test"));
    }

    @AfterAll
    public static void tearDown(){
        mongoDbContainer.close();
        mongoDbContainer.stop();
    }

    @Test
    public void testIfRepositoryIsSeeded() {
        List<Organization> organizations = organizationRepository.findAll().toStream().toList();
        Assertions.assertEquals(10, organizations.size());
    }
}
