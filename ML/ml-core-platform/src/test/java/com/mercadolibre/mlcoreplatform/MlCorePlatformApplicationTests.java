package com.mercadolibre.mlcoreplatform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for the MercadoLibre Core Platform application.
 * 
 * Note: This test requires all service dependencies to be properly configured.
 * If you encounter bean dependency issues, you can:
 * 1. Run individual controller tests instead
 * 2. Use the MlCorePlatformApplicationLightTest for basic validation
 * 3. Configure missing API adapters
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MlCorePlatformApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        // All required beans should be properly configured
    }

}
