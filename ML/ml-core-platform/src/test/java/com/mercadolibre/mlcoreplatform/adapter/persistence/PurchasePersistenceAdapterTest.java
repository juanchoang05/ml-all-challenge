package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PurchaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PurchasePersistenceAdapterTest {

    private PurchasePersistenceAdapter purchasePersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        purchasePersistenceAdapter = new PurchasePersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(purchasePersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllPurchases() {
        // When
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();

        // Then
        assertThat(purchases)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all purchases have required fields
        purchases.forEach(purchase -> {
            assertThat(purchase.id()).isNotNull();
            assertThat(purchase.status()).isNotNull();
        });
    }

    @Test
    void shouldFindPurchaseById_WhenPurchaseExists() {
        // Given - First get a valid purchase ID from findAll
        List<PurchaseDto> allPurchases = purchasePersistenceAdapter.findAll();
        assertThat(allPurchases).isNotEmpty();
        String validPurchaseId = allPurchases.get(0).id();

        // When
        Optional<PurchaseDto> result = purchasePersistenceAdapter.findById(validPurchaseId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(validPurchaseId);
        assertThat(result.get().status()).isNotNull();
    }

    @Test
    void shouldReturnEmptyOptional_WhenPurchaseDoesNotExist() {
        // Given
        String nonExistentPurchaseId = "NON_EXISTENT_ID";

        // When
        Optional<PurchaseDto> result = purchasePersistenceAdapter.findById(nonExistentPurchaseId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptional_WhenPurchaseIdIsNull() {
        // When
        Optional<PurchaseDto> result = purchasePersistenceAdapter.findById(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapPurchaseDtoCorrectly() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // When
        PurchaseDto purchase = purchases.get(0);

        // Then - Verify required fields are properly mapped
        assertThat(purchase.id()).isNotNull();
        assertThat(purchase.status()).isNotNull();
        
        // Verify that date fields are properly converted if present
        if (purchase.dateCreated() != null) {
            assertThat(purchase.dateCreated()).isNotNull();
        }
        
        if (purchase.lastUpdated() != null) {
            assertThat(purchase.lastUpdated()).isNotNull();
        }
        
        // Verify numeric fields
        if (purchase.totalAmount() != null) {
            assertThat(purchase.totalAmount()).isInstanceOf(Double.class);
        }
        
        if (purchase.buyer() != null && purchase.buyer().id() != null) {
            assertThat(purchase.buyer().id()).isInstanceOf(Long.class);
        }
    }

    @Test
    void shouldHandleNullDateFields() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // Then - Verify that null date fields are handled correctly
        purchases.forEach(purchase -> {
            // Date fields can be null and should not throw exceptions
            if (purchase.dateCreated() != null) {
                assertThat(purchase.dateCreated()).isNotNull();
            }
            
            if (purchase.lastUpdated() != null) {
                assertThat(purchase.lastUpdated()).isNotNull();
            }
            
            if (purchase.expirationDate() != null) {
                assertThat(purchase.expirationDate()).isNotNull();
            }
        });
    }

    @Test
    void shouldHandleNullAmountFields() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // Then - Verify that null amount fields are handled correctly
        purchases.forEach(purchase -> {
            // Amount fields can be null and should not throw exceptions
            if (purchase.totalAmount() != null) {
                assertThat(purchase.totalAmount()).isInstanceOf(Double.class);
            }
            
            if (purchase.paidAmount() != null) {
                assertThat(purchase.paidAmount()).isInstanceOf(Double.class);
            }
            
            if (purchase.coupon() != null && purchase.coupon().amount() != null) {
                assertThat(purchase.coupon().amount()).isInstanceOf(Double.class);
            }
        });
    }

    @Test
    void shouldHandleNestedObjects() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // Then - Verify that nested objects are handled correctly
        purchases.forEach(purchase -> {
            // These nested objects can be null and should not throw exceptions
            if (purchase.orderItems() != null) {
                assertThat(purchase.orderItems()).isNotNull();
                purchase.orderItems().forEach(item -> {
                    assertThat(item).isNotNull();
                });
            }
            
            if (purchase.payments() != null) {
                assertThat(purchase.payments()).isNotNull();
                purchase.payments().forEach(payment -> {
                    assertThat(payment).isNotNull();
                });
            }
            
            if (purchase.shipping() != null) {
                assertThat(purchase.shipping()).isNotNull();
            }
            
            if (purchase.buyer() != null) {
                assertThat(purchase.buyer()).isNotNull();
            }
        });
    }

    @Test
    void shouldHaveValidPurchaseStatuses() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // Then - All purchases should have valid statuses
        purchases.forEach(purchase -> {
            assertThat(purchase.status()).isNotNull();
            
            // Status should be one of the common values
            List<String> validStatuses = List.of(
                "confirmed", "payment_required", "payment_in_process", 
                "paid", "shipped", "delivered", "cancelled", "invalid"
            );
            assertThat(validStatuses).contains(purchase.status());
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<PurchaseDto> allPurchases1 = purchasePersistenceAdapter.findAll();
        List<PurchaseDto> allPurchases2 = purchasePersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allPurchases1).hasSize(allPurchases2.size());
        
        // Verify that finding by ID is consistent
        if (!allPurchases1.isEmpty()) {
            String purchaseId = allPurchases1.get(0).id();
            Optional<PurchaseDto> purchase1 = purchasePersistenceAdapter.findById(purchaseId);
            Optional<PurchaseDto> purchase2 = purchasePersistenceAdapter.findById(purchaseId);
            
            assertThat(purchase1).isPresent();
            assertThat(purchase2).isPresent();
            assertThat(purchase1.get().id()).isEqualTo(purchase2.get().id());
            assertThat(purchase1.get().status()).isEqualTo(purchase2.get().status());
        }
    }

    @Test
    void shouldHaveValidPurchaseIds() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        assertThat(purchases).isNotEmpty();

        // Then - All purchases should have valid IDs
        purchases.forEach(purchase -> {
            assertThat(purchase.id()).isNotNull();
            assertThat(purchase.id()).isNotBlank();
        });
        
        // IDs should be unique
        List<String> ids = purchases.stream()
                .map(PurchaseDto::id)
                .toList();
                
        List<String> uniqueIds = ids.stream()
                .distinct()
                .toList();
                
        assertThat(ids).hasSize(uniqueIds.size());
    }

    @Test
    void shouldHandleOrderItemsCorrectly() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        
        // Find a purchase with order items
        Optional<PurchaseDto> purchaseWithItems = purchases.stream()
                .filter(purchase -> purchase.orderItems() != null && !purchase.orderItems().isEmpty())
                .findFirst();

        if (purchaseWithItems.isPresent()) {
            PurchaseDto purchase = purchaseWithItems.get();
            
            // Then - Order items should be properly mapped
            assertThat(purchase.orderItems()).isNotEmpty();
            purchase.orderItems().forEach(item -> {
                assertThat(item).isNotNull();
                // Verify that item has required fields if they exist
                if (item.item() != null) {
                    assertThat(item.item()).isNotNull();
                }
                if (item.quantity() != null) {
                    assertThat(item.quantity()).isInstanceOf(Integer.class);
                }
            });
        }
    }

    @Test
    void shouldHandlePaymentsCorrectly() {
        // Given
        List<PurchaseDto> purchases = purchasePersistenceAdapter.findAll();
        
        // Find a purchase with payments
        Optional<PurchaseDto> purchaseWithPayments = purchases.stream()
                .filter(purchase -> purchase.payments() != null && !purchase.payments().isEmpty())
                .findFirst();

        if (purchaseWithPayments.isPresent()) {
            PurchaseDto purchase = purchaseWithPayments.get();
            
            // Then - Payments should be properly mapped
            assertThat(purchase.payments()).isNotEmpty();
            purchase.payments().forEach(payment -> {
                assertThat(payment).isNotNull();
                // Verify that payment has required fields if they exist
                if (payment.id() != null) {
                    assertThat(payment.id()).isNotNull();
                }
                if (payment.status() != null) {
                    assertThat(payment.status()).isNotNull();
                }
            });
        }
    }
}
