package com.orangeandbronze.enlistment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
class AbstractControllerIT {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MockMvc mockMvc;
    private final static String TEST = "TEST";

    @Container
    private final PostgreSQLContainer container = new PostgreSQLContainer("postgres:15")
            .withDatabaseName(TEST).withUsername(TEST).withPassword(TEST);

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:15:///" + TEST);
        registry.add("spring.datasource.password", () -> TEST);
        registry.add("spring.datasource.username", () -> TEST);
        registry.add("spring.jpa.hibernate.ddl-auto", ()->"create");
    }
}
