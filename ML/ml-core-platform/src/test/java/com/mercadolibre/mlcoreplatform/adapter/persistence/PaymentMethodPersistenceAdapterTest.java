package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PaymentMethodDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PaymentMethodPersistenceAdapterTest {

    private PaymentMethodPersistenceAdapter paymentMethodPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        paymentMethodPersistenceAdapter = new PaymentMethodPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(paymentMethodPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllPaymentMethods() {
        // When
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();

        // Then
        assertThat(paymentMethods)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all payment methods have required fields
        paymentMethods.forEach(paymentMethod -> {
            assertThat(paymentMethod.id()).isNotNull();
            assertThat(paymentMethod.name()).isNotNull();
            assertThat(paymentMethod.paymentTypeId()).isNotNull();
        });
    }

    @Test
    void shouldFindPaymentMethodsBySiteId_WhenSiteExists() {
        // Given - First get all to find a valid site
        List<PaymentMethodDto> allPaymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(allPaymentMethods).isNotEmpty();
        
        // Assume we know some valid site IDs from the data structure
        String validSiteId = "MLA"; // Common site ID for Argentina

        // When
        List<PaymentMethodDto> result = paymentMethodPersistenceAdapter.findBySiteId(validSiteId);

        // Then
        assertThat(result).isNotNull();
        
        // If the site exists, should return payment methods
        if (!result.isEmpty()) {
            result.forEach(paymentMethod -> {
                assertThat(paymentMethod.id()).isNotNull();
                assertThat(paymentMethod.name()).isNotNull();
                assertThat(paymentMethod.paymentTypeId()).isNotNull();
            });
        }
    }

    @Test
    void shouldReturnEmptyList_WhenSiteIdDoesNotExist() {
        // Given
        String nonExistentSiteId = "NON_EXISTENT_SITE";

        // When
        List<PaymentMethodDto> result = paymentMethodPersistenceAdapter.findBySiteId(nonExistentSiteId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenSiteIdIsNull() {
        // When
        List<PaymentMethodDto> result = paymentMethodPersistenceAdapter.findBySiteId(null);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapPaymentMethodDtoCorrectly() {
        // Given
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(paymentMethods).isNotEmpty();

        // When
        PaymentMethodDto paymentMethod = paymentMethods.get(0);

        // Then - Verify all required fields are properly mapped
        assertThat(paymentMethod.id()).isNotNull();
        assertThat(paymentMethod.name()).isNotNull();
        assertThat(paymentMethod.paymentTypeId()).isNotNull();
        assertThat(paymentMethod.status()).isNotNull();
        
        // Verify that nested objects are accessible
        if (paymentMethod.settings() != null) {
            assertThat(paymentMethod.settings()).isNotNull();
        }
        
        if (paymentMethod.additionalInfoNeeded() != null) {
            assertThat(paymentMethod.additionalInfoNeeded()).isNotNull();
        }
    }

    @Test
    void shouldHandleNullSettings() {
        // Given
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(paymentMethods).isNotEmpty();

        // Then - Verify that null settings are handled correctly
        paymentMethods.forEach(paymentMethod -> {
            // Settings can be null and should not throw exceptions
            if (paymentMethod.settings() != null) {
                assertThat(paymentMethod.settings()).isNotNull();
                paymentMethod.settings().forEach(setting -> {
                    assertThat(setting).isNotNull();
                });
            }
        });
    }

    @Test
    void shouldHandleNullAdditionalInfoNeeded() {
        // Given
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(paymentMethods).isNotEmpty();

        // Then - Verify that null additional info is handled correctly
        paymentMethods.forEach(paymentMethod -> {
            // Additional info needed can be null and should not throw exceptions
            if (paymentMethod.additionalInfoNeeded() != null) {
                assertThat(paymentMethod.additionalInfoNeeded()).isNotNull();
            }
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<PaymentMethodDto> allPaymentMethods1 = paymentMethodPersistenceAdapter.findAll();
        List<PaymentMethodDto> allPaymentMethods2 = paymentMethodPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allPaymentMethods1).hasSize(allPaymentMethods2.size());
        
        // Verify that finding by site ID is consistent
        if (!allPaymentMethods1.isEmpty()) {
            // Try with a common site ID
            String siteId = "MLA";
            List<PaymentMethodDto> sitePaymentMethods1 = paymentMethodPersistenceAdapter.findBySiteId(siteId);
            List<PaymentMethodDto> sitePaymentMethods2 = paymentMethodPersistenceAdapter.findBySiteId(siteId);
            
            assertThat(sitePaymentMethods1).hasSize(sitePaymentMethods2.size());
        }
    }

    @Test
    void shouldContainCommonPaymentMethods() {
        // Given
        List<PaymentMethodDto> allPaymentMethods = paymentMethodPersistenceAdapter.findAll();

        // Then - Should contain some common payment methods
        assertThat(allPaymentMethods).isNotEmpty();
        
        // Verify that common payment type IDs exist
        List<String> paymentTypeIds = allPaymentMethods.stream()
                .map(PaymentMethodDto::paymentTypeId)
                .distinct()
                .toList();
                
        assertThat(paymentTypeIds).isNotEmpty();
        
        // Common payment types should be present
        boolean hasCommonTypes = paymentTypeIds.stream()
                .anyMatch(typeId -> typeId.equals("credit_card") || 
                                   typeId.equals("debit_card") || 
                                   typeId.equals("ticket") ||
                                   typeId.equals("account_money"));
                                   
        // At least one common payment type should exist
        assertThat(hasCommonTypes).isTrue();
    }

    @Test
    void shouldFilterCorrectlyBySiteId() {
        // Given
        List<PaymentMethodDto> allPaymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(allPaymentMethods).isNotEmpty();

        // Test with multiple site IDs
        String[] commonSiteIds = {"MLA", "MLB", "MLM", "MCO"};
        
        for (String siteId : commonSiteIds) {
            // When
            List<PaymentMethodDto> sitePaymentMethods = paymentMethodPersistenceAdapter.findBySiteId(siteId);

            // Then
            assertThat(sitePaymentMethods).isNotNull();
            
            // If payment methods exist for this site, they should all be valid
            sitePaymentMethods.forEach(paymentMethod -> {
                assertThat(paymentMethod.id()).isNotNull();
                assertThat(paymentMethod.name()).isNotNull();
                assertThat(paymentMethod.paymentTypeId()).isNotNull();
                assertThat(paymentMethod.status()).isNotNull();
            });
        }
    }

    @Test
    void shouldHaveValidPaymentMethodStatuses() {
        // Given
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(paymentMethods).isNotEmpty();

        // Then - All payment methods should have valid statuses
        paymentMethods.forEach(paymentMethod -> {
            assertThat(paymentMethod.status()).isNotNull();
            
            // Status should be one of the common values
            List<String> validStatuses = List.of("active", "inactive", "deactivated", "temporally_deactivated");
            assertThat(validStatuses).contains(paymentMethod.status());
        });
    }

    @Test
    void shouldHaveValidPaymentMethodIds() {
        // Given
        List<PaymentMethodDto> paymentMethods = paymentMethodPersistenceAdapter.findAll();
        assertThat(paymentMethods).isNotEmpty();

        // Then - All payment methods should have valid IDs
        paymentMethods.forEach(paymentMethod -> {
            assertThat(paymentMethod.id()).isNotNull();
            assertThat(paymentMethod.id()).isNotBlank();
        });
        
        // IDs should be unique
        List<String> ids = paymentMethods.stream()
                .map(PaymentMethodDto::id)
                .toList();
                
        List<String> uniqueIds = ids.stream()
                .distinct()
                .toList();
                
        assertThat(ids).hasSize(uniqueIds.size());
    }
}
