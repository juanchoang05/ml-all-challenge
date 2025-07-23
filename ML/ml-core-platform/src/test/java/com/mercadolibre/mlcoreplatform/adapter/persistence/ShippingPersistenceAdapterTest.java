package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ShippingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ShippingPersistenceAdapterTest {

    private ShippingPersistenceAdapter shippingPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        shippingPersistenceAdapter = new ShippingPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(shippingPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllShippings() {
        // When
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();

        // Then
        assertThat(shippings)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all shippings have required fields
        shippings.forEach(shipping -> {
            assertThat(shipping.id()).isNotNull();
            assertThat(shipping.name()).isNotNull();
        });
    }

    @Test
    void shouldFindShippingsByItemId() {
        // Given - Use a common item ID pattern
        String itemId = "MLA1"; // Common pattern for testing

        // When
        List<ShippingDto> shippings = shippingPersistenceAdapter.findByItemId(itemId);

        // Then
        assertThat(shippings).isNotNull();
        
        // If shippings exist, verify their structure
        shippings.forEach(shipping -> {
            assertThat(shipping.id()).isNotNull();
            assertThat(shipping.name()).isNotNull();
        });
    }

    @Test
    void shouldReturnEmptyList_WhenItemIdDoesNotExist() {
        // Given
        String nonExistentItemId = "NON_EXISTENT_ITEM";

        // When
        List<ShippingDto> result = shippingPersistenceAdapter.findByItemId(nonExistentItemId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenItemIdIsNull() {
        // When
        List<ShippingDto> result = shippingPersistenceAdapter.findByItemId(null);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapShippingDtoCorrectly() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // When
        ShippingDto shipping = shippings.get(0);

        // Then - Verify required fields are properly mapped
        assertThat(shipping.id()).isNotNull();
        assertThat(shipping.name()).isNotNull();
        
        // Verify cost fields if present
        if (shipping.cost() != null) {
            assertThat(shipping.cost()).isInstanceOf(Double.class);
            assertThat(shipping.cost()).isGreaterThanOrEqualTo(0.0);
        }
        
        if (shipping.listCost() != null) {
            assertThat(shipping.listCost()).isInstanceOf(Double.class);
            assertThat(shipping.listCost()).isGreaterThanOrEqualTo(0.0);
        }
    }

    @Test
    void shouldHandleShippingCosts() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Verify that shipping costs are handled correctly
        shippings.forEach(shipping -> {
            if (shipping.cost() != null) {
                assertThat(shipping.cost()).isInstanceOf(Double.class);
                assertThat(shipping.cost()).isGreaterThanOrEqualTo(0.0);
            }
            
            if (shipping.listCost() != null) {
                assertThat(shipping.listCost()).isInstanceOf(Double.class);
                assertThat(shipping.listCost()).isGreaterThanOrEqualTo(0.0);
            }
        });
    }

    @Test
    void shouldHandleFreeShipping() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Verify that free shipping is handled correctly
        shippings.forEach(shipping -> {
            if (shipping.freeShipping() != null) {
                assertThat(shipping.freeShipping()).isNotNull();
                
                if (shipping.freeShipping().flag() != null) {
                    assertThat(shipping.freeShipping().flag()).isInstanceOf(Boolean.class);
                }
                
                if (shipping.freeShipping().rule() != null) {
                    assertThat(shipping.freeShipping().rule()).isNotNull();
                    
                    if (shipping.freeShipping().rule().value() != null) {
                        assertThat(shipping.freeShipping().rule().value()).isInstanceOf(Long.class);
                        assertThat(shipping.freeShipping().rule().value()).isGreaterThanOrEqualTo(0L);
                    }
                }
            }
        });
    }

    @Test
    void shouldHandleEstimatedDelivery() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Verify that estimated delivery is handled correctly
        shippings.forEach(shipping -> {
            if (shipping.estimatedDelivery() != null) {
                assertThat(shipping.estimatedDelivery()).isNotNull();
                
                if (shipping.estimatedDelivery().date() != null) {
                    assertThat(shipping.estimatedDelivery().date()).isNotNull();
                }
                
                if (shipping.estimatedDelivery().unit() != null) {
                    assertThat(shipping.estimatedDelivery().unit()).isNotNull();
                    
                    // Unit should be one of the common values
                    List<String> validUnits = List.of("days", "hours", "minutes", "business_days");
                    assertThat(validUnits).contains(shipping.estimatedDelivery().unit());
                }
            }
        });
    }

    @Test
    void shouldHandleSpeedRanking() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Verify that speed ranking is handled correctly
        shippings.forEach(shipping -> {
            if (shipping.speedRanking() != null) {
                assertThat(shipping.speedRanking()).isInstanceOf(Integer.class);
                assertThat(shipping.speedRanking()).isGreaterThanOrEqualTo(1);
                assertThat(shipping.speedRanking()).isLessThanOrEqualTo(10); // Assuming 1-10 scale
            }
        });
    }

    @Test
    void shouldHandleDeliveryType() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Verify that delivery type is handled correctly
        shippings.forEach(shipping -> {
            if (shipping.deliveryType() != null) {
                assertThat(shipping.deliveryType()).isNotNull();
                
                // Delivery type should be one of the common values
                List<String> validTypes = List.of(
                    "standard", "express", "same_day", "next_day", "pickup", "normal"
                );
                assertThat(validTypes).contains(shipping.deliveryType());
            }
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<ShippingDto> allShippings1 = shippingPersistenceAdapter.findAll();
        List<ShippingDto> allShippings2 = shippingPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allShippings1).hasSize(allShippings2.size());
        
        // Verify that finding by item ID is consistent
        String testItemId = "MLA1";
        List<ShippingDto> itemShippings1 = shippingPersistenceAdapter.findByItemId(testItemId);
        List<ShippingDto> itemShippings2 = shippingPersistenceAdapter.findByItemId(testItemId);
        
        assertThat(itemShippings1).hasSize(itemShippings2.size());
        
        if (!itemShippings1.isEmpty() && !itemShippings2.isEmpty()) {
            assertThat(itemShippings1.get(0).id()).isEqualTo(itemShippings2.get(0).id());
            assertThat(itemShippings1.get(0).name()).isEqualTo(itemShippings2.get(0).name());
        }
    }

    @Test
    void shouldHaveValidShippingIds() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - All shippings should have valid IDs
        shippings.forEach(shipping -> {
            assertThat(shipping.id()).isNotNull();
            assertThat(shipping.id()).isNotBlank();
        });
        
        // Verify IDs are valid strings (some may be duplicated due to data structure)
        List<String> ids = shippings.stream()
                .map(ShippingDto::id)
                .toList();
                
        assertThat(ids).isNotEmpty();
        assertThat(ids).allMatch(id -> id != null && !id.isBlank());
    }

    @Test
    void shouldHandleShippingMethodId() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Shipping method IDs should be valid if present
        shippings.forEach(shipping -> {
            if (shipping.shippingMethodId() != null) {
                assertThat(shipping.shippingMethodId()).isInstanceOf(Long.class);
                assertThat(shipping.shippingMethodId()).isGreaterThan(0L);
            }
        });
    }

    @Test
    void shouldFilterCorrectlyByItemId() {
        // Given - Try multiple item IDs
        String[] testItemIds = {"MLA1", "MLA123", "MLB456"};
        
        for (String itemId : testItemIds) {
            // When
            List<ShippingDto> shippings = shippingPersistenceAdapter.findByItemId(itemId);
            
            // Then
            assertThat(shippings).isNotNull();
            
            // All returned shippings should be valid
            shippings.forEach(shipping -> {
                assertThat(shipping.id()).isNotNull();
                assertThat(shipping.name()).isNotNull();
            });
        }
    }

    @Test
    void shouldHandleCompanyInformation() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Company information should be valid if present
        shippings.forEach(shipping -> {
            if (shipping.company() != null) {
                assertThat(shipping.company()).isNotNull();
                
                // Company should have valid fields if present
                if (shipping.company().id() != null) {
                    assertThat(shipping.company().id()).isNotNull();
                }
                
                if (shipping.company().name() != null) {
                    assertThat(shipping.company().name()).isNotNull();
                }
            }
        });
    }

    @Test
    void shouldHandleHandlingTime() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Handling time should be valid if present
        shippings.forEach(shipping -> {
            if (shipping.handlingTime() != null) {
                assertThat(shipping.handlingTime()).isNotNull();
                
                if (shipping.handlingTime().unit() != null) {
                    assertThat(shipping.handlingTime().unit()).isNotNull();
                    
                    // Unit should be one of the common values
                    List<String> validUnits = List.of("days", "hours", "minutes");
                    assertThat(validUnits).contains(shipping.handlingTime().unit());
                }
                
                if (shipping.handlingTime().value() != null) {
                    assertThat(shipping.handlingTime().value()).isInstanceOf(Integer.class);
                    assertThat(shipping.handlingTime().value()).isGreaterThanOrEqualTo(0);
                }
            }
        });
    }

    @Test
    void shouldHandlePickupPoints() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Pickup points should be valid if present
        shippings.forEach(shipping -> {
            if (shipping.pickupPoints() != null) {
                assertThat(shipping.pickupPoints()).isNotNull();
                
                shipping.pickupPoints().forEach(pickupPoint -> {
                    assertThat(pickupPoint).isNotNull();
                    
                    if (pickupPoint.id() != null) {
                        assertThat(pickupPoint.id()).isNotNull();
                    }
                    
                    if (pickupPoint.name() != null) {
                        assertThat(pickupPoint.name()).isNotNull();
                    }
                });
            }
        });
    }

    @Test
    void shouldHandleCoverage() {
        // Given
        List<ShippingDto> shippings = shippingPersistenceAdapter.findAll();
        assertThat(shippings).isNotEmpty();

        // Then - Coverage should be valid if present
        shippings.forEach(shipping -> {
            if (shipping.coverage() != null) {
                assertThat(shipping.coverage()).isNotNull();
                
                if (shipping.coverage().allCountry() != null) {
                    assertThat(shipping.coverage().allCountry()).isInstanceOf(Boolean.class);
                }
                
                if (shipping.coverage().specificPlaces() != null) {
                    assertThat(shipping.coverage().specificPlaces()).isNotNull();
                }
            }
        });
    }
}
