package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.SellerDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.SellerPersistencePort;
import com.mercadolibre.mlcoreplatform.domain.port.out.UserApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private SellerPersistencePort sellerPersistencePort;
    
    @Mock
    private UserApiPort userApiPort;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService(sellerPersistencePort, userApiPort);
    }
    
    @Test
    void getAllUsers_ShouldReturnPaginatedUsers() {
        // Given
        List<SellerDto> mockSellers = Arrays.asList(
            createMockSellerDto(1L, "john_doe", "John", "Doe"),
            createMockSellerDto(2L, "jane_smith", "Jane", "Smith"),
            createMockSellerDto(3L, "bob_wilson", "Bob", "Wilson")
        );
        when(sellerPersistencePort.findAll()).thenReturn(mockSellers);
        
        // When
        List<UserResponse> result = userService.getAllUsers(0, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("john_doe", result.get(0).nickname());
        assertEquals("John", result.get(0).firstName());
        assertEquals("2", result.get(1).id());
        assertEquals("jane_smith", result.get(1).nickname());
        verify(sellerPersistencePort).findAll();
    }
    
    @Test
    void getAllUsers_WithOffset_ShouldSkipCorrectNumber() {
        // Given
        List<SellerDto> mockSellers = Arrays.asList(
            createMockSellerDto(1L, "john_doe", "John", "Doe"),
            createMockSellerDto(2L, "jane_smith", "Jane", "Smith"),
            createMockSellerDto(3L, "bob_wilson", "Bob", "Wilson")
        );
        when(sellerPersistencePort.findAll()).thenReturn(mockSellers);
        
        // When
        List<UserResponse> result = userService.getAllUsers(1, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2", result.get(0).id());
        assertEquals("3", result.get(1).id());
        verify(sellerPersistencePort).findAll();
    }
    
    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Given
        when(sellerPersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<UserResponse> result = userService.getAllUsers(0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sellerPersistencePort).findAll();
    }
    
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        String userId = "123";
        SellerDto mockSeller = createMockSellerDto(123L, "john_doe", "John", "Doe");
        when(sellerPersistencePort.findById(123L)).thenReturn(Optional.of(mockSeller));
        
        // When
        UserResponse result = userService.getUserById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals("john_doe", result.nickname());
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        verify(sellerPersistencePort).findById(123L);
    }
    
    @Test
    void getUserById_WhenUserNotFound_ShouldThrowException() {
        // Given
        String userId = "999";
        when(sellerPersistencePort.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.getUserById(userId));
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(sellerPersistencePort).findById(999L);
    }
    
    @Test
    void getUserItems_ShouldReturnUserProducts() {
        // Given
        String userId = "123";
        int offset = 0;
        int limit = 10;
        List<ProductResponse> mockProducts = Arrays.asList(
            ProductResponse.builder()
                .id("MLA123456")
                .title("iPhone 15 Pro")
                .price(BigDecimal.valueOf(1500))
                .sellerId(userId)
                .build(),
            ProductResponse.builder()
                .id("MLA789012")
                .title("Samsung Galaxy S24")
                .price(BigDecimal.valueOf(1200))
                .sellerId(userId)
                .build()
        );
        when(userApiPort.getUserItems(userId, offset, limit)).thenReturn(mockProducts);
        
        // When
        List<ProductResponse> result = userService.getUserItems(userId, offset, limit);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA123456", result.get(0).id());
        assertEquals("iPhone 15 Pro", result.get(0).title());
        assertEquals(userId, result.get(0).sellerId());
        verify(userApiPort).getUserItems(userId, offset, limit);
    }
    
    @Test
    void getUserItems_WhenUserHasNoItems_ShouldReturnEmptyList() {
        // Given
        String userId = "123";
        when(userApiPort.getUserItems(userId, 0, 10)).thenReturn(Collections.emptyList());
        
        // When
        List<ProductResponse> result = userService.getUserItems(userId, 0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userApiPort).getUserItems(userId, 0, 10);
    }
    
    @Test
    void getUserPaymentMethods_ShouldReturnPaymentMethods() {
        // Given
        String userId = "123";
        List<PaymentMethodResponse> mockPaymentMethods = Arrays.asList(
            PaymentMethodResponse.builder()
                .id("visa")
                .name("Visa")
                .paymentTypeId("credit_card")
                .status("active")
                .build(),
            PaymentMethodResponse.builder()
                .id("master")
                .name("Mastercard")
                .paymentTypeId("credit_card")
                .status("active")
                .build()
        );
        when(userApiPort.getUserPaymentMethods(userId)).thenReturn(mockPaymentMethods);
        
        // When
        List<PaymentMethodResponse> result = userService.getUserPaymentMethods(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("visa", result.get(0).id());
        assertEquals("Visa", result.get(0).name());
        assertEquals("credit_card", result.get(0).paymentTypeId());
        verify(userApiPort).getUserPaymentMethods(userId);
    }
    
    @Test
    void getUserPaymentMethods_WhenUserHasNoPaymentMethods_ShouldReturnEmptyList() {
        // Given
        String userId = "123";
        when(userApiPort.getUserPaymentMethods(userId)).thenReturn(Collections.emptyList());
        
        // When
        List<PaymentMethodResponse> result = userService.getUserPaymentMethods(userId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userApiPort).getUserPaymentMethods(userId);
    }
    
    @Test
    void searchUsers_ShouldReturnSearchResults() {
        // Given
        UserSearchRequest request = UserSearchRequest.builder()
            .nickname("john")
            .build();
        List<UserResponse> mockUsers = Arrays.asList(
            UserResponse.builder()
                .id("1")
                .nickname("john_doe")
                .firstName("John")
                .lastName("Doe")
                .build(),
            UserResponse.builder()
                .id("2")
                .nickname("johnny_cash")
                .firstName("Johnny")
                .lastName("Cash")
                .build()
        );
        when(userApiPort.searchUsers(request)).thenReturn(mockUsers);
        
        // When
        List<UserResponse> result = userService.searchUsers(request);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).nickname());
        assertEquals("johnny_cash", result.get(1).nickname());
        verify(userApiPort).searchUsers(request);
    }
    
    @Test
    void searchUsers_WhenNoResults_ShouldReturnEmptyList() {
        // Given
        UserSearchRequest request = UserSearchRequest.builder()
            .nickname("nonexistent")
            .build();
        when(userApiPort.searchUsers(request)).thenReturn(Collections.emptyList());
        
        // When
        List<UserResponse> result = userService.searchUsers(request);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userApiPort).searchUsers(request);
    }
    
    @Test
    void mapToUserResponse_ShouldMapAllFields() {
        // Given
        String userId = "123";
        SellerDto sellerDto = createCompleteSellerDto(123L);
        when(sellerPersistencePort.findById(123L)).thenReturn(Optional.of(sellerDto));
        
        // When
        UserResponse result = userService.getUserById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals("123", result.id());
        assertEquals("complete_user", result.nickname());
        assertEquals("Complete", result.firstName());
        assertEquals("User", result.lastName());
        assertEquals("complete@example.com", result.email());
        assertEquals("AR", result.countryId());
        assertNotNull(result.registrationDate());
        assertNotNull(result.tags());
        assertEquals(2, result.tags().size());
        assertTrue(result.tags().contains("verified"));
        assertTrue(result.tags().contains("premium"));
    }
    
    @Test
    void mapToUserResponse_WithNullOptionalFields_ShouldHandleNulls() {
        // Given
        String userId = "123";
        SellerDto sellerDto = SellerDto.builder()
            .id(123L)
            .nickname("minimal_user")
            .firstName("Minimal")
            .lastName("User")
            .email(null)
            .countryId(null)
            .registrationDate(null)
            .tags(null)
            .build();
        when(sellerPersistencePort.findById(123L)).thenReturn(Optional.of(sellerDto));
        
        // When
        UserResponse result = userService.getUserById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals("123", result.id());
        assertEquals("minimal_user", result.nickname());
        assertEquals("Minimal", result.firstName());
        assertEquals("User", result.lastName());
        assertNull(result.email());
        assertNull(result.countryId());
        assertNull(result.registrationDate());
        assertNull(result.tags());
    }
    
    @Test
    void getAllUsers_WithZeroLimit_ShouldReturnEmptyList() {
        // Given
        List<SellerDto> mockSellers = Arrays.asList(
            createMockSellerDto(1L, "john_doe", "John", "Doe")
        );
        when(sellerPersistencePort.findAll()).thenReturn(mockSellers);
        
        // When
        List<UserResponse> result = userService.getAllUsers(0, 0);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sellerPersistencePort).findAll();
    }
    
    @Test
    void getAllUsers_WithLargeOffset_ShouldReturnEmptyList() {
        // Given
        List<SellerDto> mockSellers = Arrays.asList(
            createMockSellerDto(1L, "john_doe", "John", "Doe")
        );
        when(sellerPersistencePort.findAll()).thenReturn(mockSellers);
        
        // When
        List<UserResponse> result = userService.getAllUsers(10, 5);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sellerPersistencePort).findAll();
    }
    
    private SellerDto createMockSellerDto(Long id, String nickname, String firstName, String lastName) {
        return SellerDto.builder()
            .id(id)
            .nickname(nickname)
            .firstName(firstName)
            .lastName(lastName)
            .email(nickname + "@example.com")
            .countryId("AR")
            .registrationDate(LocalDateTime.now())
            .tags(Arrays.asList("verified"))
            .build();
    }
    
    private SellerDto createCompleteSellerDto(Long id) {
        return SellerDto.builder()
            .id(id)
            .nickname("complete_user")
            .firstName("Complete")
            .lastName("User")
            .email("complete@example.com")
            .countryId("AR")
            .registrationDate(LocalDateTime.of(2024, 1, 15, 10, 30))
            .tags(Arrays.asList("verified", "premium"))
            .build();
    }
}
