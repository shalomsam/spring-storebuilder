package com.shalomsam.storebuilder;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.shalomsam.storebuilder.config.JacksonZonedDateTimeConfig;
import com.shalomsam.storebuilder.testUtils.MockGeneratorService;
import com.shalomsam.storebuilder.testUtils.MockGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

/**
 * Base Integration test.
 * Configures mongodb container and generates mocks based on MockDomainService.
 * One service per domain class to generate relevant mocks. Each mock generates mock JSON files
 * which are imported into mongodb running in the container using a shell script `mongoInti.sh`.
 *
 * @see MockGeneratorService
 * @author shalomsam
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@ComponentScan(basePackages = {"com.shalomsam.storebuilder.testUtils"})
@EnableConfigurationProperties({MockGeneratorConfig.class})
@Import({JacksonZonedDateTimeConfig.class})
@Testcontainers
public class BaseIntegrationTest {

    public static int MONGO_PORT = 27017;

    public static final int MOCK_SIZE = 10;

    public static final SecurityMockServerConfigurers.JwtMutator AUTHORITIES = mockJwt().authorities(new SimpleGrantedAuthority("user"));

    public static final Consumer<CreateContainerCmd> cmd = e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(MONGO_PORT), new ExposedPort(MONGO_PORT)));

    @Container
    private static final GenericContainer<?> mongoDbContainer = new GenericContainer<>(DockerImageName.parse("mongo:latest"))
            .withAccessToHost(true)
            .withExposedPorts(MONGO_PORT)
            .withCreateContainerCmdModifier(cmd)
            .withEnv("MONGO_INIT_DATABASE", "storebuilder-test")
            .withFileSystemBind(
                    MountableFile.forHostPath("src/test/resources/stubs/").getResolvedPath(),
                    "/stubs/",
                    BindMode.READ_WRITE
            )
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("mongoInit.sh"),
                    "/"
            );

    @BeforeAll
    public static void setUp(
        @Autowired List<MockGeneratorService<?>> mockGeneratorServices,
        @Autowired ResourceLoader resourceLoader,
        @Autowired MockGeneratorConfig mockGeneratorConfig,
        @Autowired ReactiveMongoTemplate mongoTemplate
    ) throws IOException, InterruptedException {
        mongoDbContainer.start();
        mongoDbContainer.waitingFor(
            Wait.forHealthcheck()
        );

        // Generate Mock data if not exists
        MockDataGenerator mockDataGenerator = new MockDataGenerator(mockGeneratorServices, resourceLoader, mockGeneratorConfig, mongoTemplate);
        mockDataGenerator.generateMockData(10);

        mockGeneratorServices.forEach(mockGeneratorService -> mockGeneratorService.buildMockRelationShips(mongoTemplate));
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
}
