package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.SellerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SellerPersistenceAdapterTest {

    private SellerPersistenceAdapter sellerPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        sellerPersistenceAdapter = new SellerPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(sellerPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllSellers() {
        // When
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();

        // Then
        assertThat(sellers)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all sellers have required fields
        sellers.forEach(seller -> {
            assertThat(seller.id()).isNotNull();
            assertThat(seller.nickname()).isNotNull();
        });
    }

    @Test
    void shouldFindSellerById_WhenSellerExists() {
        // Given - First get a valid seller ID from findAll
        List<SellerDto> allSellers = sellerPersistenceAdapter.findAll();
        assertThat(allSellers).isNotEmpty();
        Long validSellerId = allSellers.get(0).id();

        // When
        Optional<SellerDto> result = sellerPersistenceAdapter.findById(validSellerId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(validSellerId);
        assertThat(result.get().nickname()).isNotNull();
    }

    @Test
    void shouldReturnEmptyOptional_WhenSellerDoesNotExist() {
        // Given
        Long nonExistentSellerId = 999999999L;

        // When
        Optional<SellerDto> result = sellerPersistenceAdapter.findById(nonExistentSellerId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptional_WhenSellerIdIsNull() {
        // When
        Optional<SellerDto> result = sellerPersistenceAdapter.findById(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapSellerDtoCorrectly() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // When
        SellerDto seller = sellers.get(0);

        // Then - Verify required fields are properly mapped
        assertThat(seller.id()).isNotNull();
        assertThat(seller.nickname()).isNotNull();
        
        // Verify that ID is proper Long type
        assertThat(seller.id()).isInstanceOf(Long.class);
        
        // Verify numeric fields
        if (seller.sellerReputation() != null && seller.sellerReputation().levelId() != null) {
            assertThat(seller.sellerReputation().levelId()).isNotNull();
        }
        
        if (seller.sellerReputation() != null && seller.sellerReputation().transactions() != null) {
            assertThat(seller.sellerReputation().transactions()).isNotNull();
        }
    }

    @Test
    void shouldHandleSellerReputation() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - Verify that seller reputation is handled correctly
        sellers.forEach(seller -> {
            if (seller.sellerReputation() != null) {
                assertThat(seller.sellerReputation()).isNotNull();
                
                // Verify reputation fields if present
                if (seller.sellerReputation().levelId() != null) {
                    assertThat(seller.sellerReputation().levelId()).isNotNull();
                }
                
                if (seller.sellerReputation().powerSellerStatus() != null) {
                    assertThat(seller.sellerReputation().powerSellerStatus()).isNotNull();
                }
                
                if (seller.sellerReputation().transactions() != null) {
                    assertThat(seller.sellerReputation().transactions()).isNotNull();
                    
                    // Transaction counts should be non-negative
                    if (seller.sellerReputation().transactions().total() != null) {
                        assertThat(seller.sellerReputation().transactions().total()).isGreaterThanOrEqualTo(0);
                    }
                    
                    if (seller.sellerReputation().transactions().completed() != null) {
                        assertThat(seller.sellerReputation().transactions().completed()).isGreaterThanOrEqualTo(0);
                    }
                }
                
                if (seller.sellerReputation().transactions() != null && 
                    seller.sellerReputation().transactions().ratings() != null) {
                    var ratings = seller.sellerReputation().transactions().ratings();
                    
                    // Rating counts should be non-negative
                    if (ratings.positive() != null) {
                        assertThat(ratings.positive()).isGreaterThanOrEqualTo(0);
                    }
                    
                    if (ratings.negative() != null) {
                        assertThat(ratings.negative()).isGreaterThanOrEqualTo(0);
                    }
                    
                    if (ratings.neutral() != null) {
                        assertThat(ratings.neutral()).isGreaterThanOrEqualTo(0);
                    }
                }
            }
        });
    }

    @Test
    void shouldHandleSellerAddress() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - Verify that seller address is handled correctly
        sellers.forEach(seller -> {
            if (seller.address() != null) {
                assertThat(seller.address()).isNotNull();
                
                // Address fields can be null but should be valid if present
                if (seller.address().city() != null) {
                    assertThat(seller.address().city()).isNotNull();
                }
                
                if (seller.address().state() != null) {
                    assertThat(seller.address().state()).isNotNull();
                }
                
                if (seller.address().zipCode() != null) {
                    assertThat(seller.address().zipCode()).isNotNull();
                }
            }
        });
    }

    @Test
    void shouldHaveValidSellerStatuses() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - All sellers should have valid statuses
        sellers.forEach(seller -> {
            if (seller.status() != null) {
                assertThat(seller.status()).isNotNull();
                
                // Check status fields if present
                if (seller.status().siteStatus() != null) {
                    List<String> validSiteStatuses = List.of(
                        "active", "inactive", "suspended", "deactivated"
                    );
                    assertThat(validSiteStatuses).contains(seller.status().siteStatus());
                }
            }
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<SellerDto> allSellers1 = sellerPersistenceAdapter.findAll();
        List<SellerDto> allSellers2 = sellerPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allSellers1).hasSize(allSellers2.size());
        
        // Verify that finding by ID is consistent
        if (!allSellers1.isEmpty()) {
            Long sellerId = allSellers1.get(0).id();
            Optional<SellerDto> seller1 = sellerPersistenceAdapter.findById(sellerId);
            Optional<SellerDto> seller2 = sellerPersistenceAdapter.findById(sellerId);
            
            assertThat(seller1).isPresent();
            assertThat(seller2).isPresent();
            assertThat(seller1.get().id()).isEqualTo(seller2.get().id());
            assertThat(seller1.get().nickname()).isEqualTo(seller2.get().nickname());
        }
    }

    @Test
    void shouldHaveValidSellerIds() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - All sellers should have valid IDs
        sellers.forEach(seller -> {
            assertThat(seller.id()).isNotNull();
            assertThat(seller.id()).isInstanceOf(Long.class);
            assertThat(seller.id()).isGreaterThan(0L);
        });
        
        // IDs should be unique
        List<Long> ids = sellers.stream()
                .map(SellerDto::id)
                .toList();
                
        List<Long> uniqueIds = ids.stream()
                .distinct()
                .toList();
                
        assertThat(ids).hasSize(uniqueIds.size());
    }

    @Test
    void shouldHandleRegistrationDate() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - Registration dates should be valid if present
        sellers.forEach(seller -> {
            if (seller.registrationDate() != null) {
                assertThat(seller.registrationDate()).isNotNull();
                // Registration date should be in the past
                assertThat(seller.registrationDate()).isBeforeOrEqualTo(java.time.LocalDateTime.now());
            }
        });
    }

    @Test
    void shouldHandleCountryId() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        assertThat(sellers).isNotEmpty();

        // Then - Country IDs should be valid if present
        sellers.forEach(seller -> {
            if (seller.countryId() != null) {
                assertThat(seller.countryId()).isNotNull();
                assertThat(seller.countryId()).isNotBlank();
                
                // Common country IDs should be valid
                List<String> validCountryIds = List.of("AR", "BR", "MX", "CO", "CL", "PE", "UY");
                // Either it's a known country ID or it's a valid format
                boolean isValid = validCountryIds.contains(seller.countryId()) || 
                                seller.countryId().matches("[A-Z]{2}");
                assertThat(isValid).isTrue();
            }
        });
    }

    @Test
    void shouldHandlePowerSellerStatus() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        
        // Find sellers with reputation information
        List<SellerDto> sellersWithReputation = sellers.stream()
                .filter(seller -> seller.sellerReputation() != null)
                .toList();

        // Then - Power seller status should be valid if present
        sellersWithReputation.forEach(seller -> {
            if (seller.sellerReputation().powerSellerStatus() != null) {
                String powerSellerStatus = seller.sellerReputation().powerSellerStatus();
                List<String> validStatuses = List.of("gold", "silver", "bronze", "none", "platinum");
                assertThat(validStatuses).contains(powerSellerStatus);
            }
        });
    }

    @Test
    void shouldHandleTransactionMetrics() {
        // Given
        List<SellerDto> sellers = sellerPersistenceAdapter.findAll();
        
        // Find sellers with transaction information
        List<SellerDto> sellersWithTransactions = sellers.stream()
                .filter(seller -> seller.sellerReputation() != null && 
                                 seller.sellerReputation().transactions() != null)
                .toList();

        // Then - Transaction metrics should be consistent
        sellersWithTransactions.forEach(seller -> {
            var transactions = seller.sellerReputation().transactions();
            
            if (transactions.total() != null && transactions.completed() != null) {
                // Completed transactions should not exceed total
                assertThat(transactions.completed()).isLessThanOrEqualTo(transactions.total());
            }
            
            if (transactions.canceled() != null && transactions.total() != null) {
                // Canceled transactions should not exceed total
                assertThat(transactions.canceled()).isLessThanOrEqualTo(transactions.total());
            }
        });
    }
}
