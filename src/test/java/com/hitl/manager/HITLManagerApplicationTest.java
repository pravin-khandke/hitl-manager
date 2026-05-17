package com.hitl.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class HITLManagerApplicationTest {

    @Test
    void contextLoads() {
        // Verifies Spring context starts successfully with H2 in-memory database
    }
}
