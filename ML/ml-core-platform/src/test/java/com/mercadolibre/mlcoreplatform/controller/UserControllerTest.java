package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.UserUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.UserController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserUseCase userUseCase;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        List<UserResponse> mockResponse = Arrays.asList(
                UserResponse.builder()
                        .id("USER123")
                        .nickname("johndoe")
                        .firstName("John")
                        .lastName("Doe")
                        .build(),
                UserResponse.builder()
                        .id("USER456")
                        .nickname("janedoe")
                        .firstName("Jane")
                        .lastName("Doe")
                        .build()
        );

        when(userUseCase.getAllUsers(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("USER123"))
                .andExpect(jsonPath("$[0].nickname").value("johndoe"))
                .andExpect(jsonPath("$[1].id").value("USER456"))
                .andExpect(jsonPath("$[1].nickname").value("janedoe"));
    }

    @Test
    void testGetAllUsersWithDefaultParams() throws Exception {
        // Arrange
        List<UserResponse> mockResponse = Arrays.asList(
                UserResponse.builder()
                        .id("USER123")
                        .nickname("johndoe")
                        .build()
        );

        when(userUseCase.getAllUsers(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetUserById() throws Exception {
        // Arrange
        String userId = "USER123";
        UserResponse mockResponse = UserResponse.builder()
                .id(userId)
                .nickname("johndoe")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        when(userUseCase.getUserById(userId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value("johndoe"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserItems() throws Exception {
        // Arrange
        String userId = "USER123";
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("ITEM123")
                        .title("iPhone 14")
                        .price(BigDecimal.valueOf(999.99))
                        .build(),
                ProductResponse.builder()
                        .id("ITEM456")
                        .title("Samsung Galaxy")
                        .price(BigDecimal.valueOf(799.99))
                        .build()
        );

        when(userUseCase.getUserItems(userId, 0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}/items/search", userId)
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("ITEM123"))
                .andExpect(jsonPath("$[0].title").value("iPhone 14"))
                .andExpect(jsonPath("$[1].id").value("ITEM456"));
    }

    @Test
    void testGetUserPaymentMethods() throws Exception {
        // Arrange
        String userId = "USER123";
        List<PaymentMethodResponse> mockResponse = Arrays.asList(
                PaymentMethodResponse.builder()
                        .id("visa")
                        .name("Visa")
                        .paymentTypeId("credit_card")
                        .build(),
                PaymentMethodResponse.builder()
                        .id("mastercard")
                        .name("Mastercard")
                        .paymentTypeId("credit_card")
                        .build()
        );

        when(userUseCase.getUserPaymentMethods(userId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}/accepted_payment_methods", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("visa"))
                .andExpect(jsonPath("$[1].id").value("mastercard"));
    }

    @Test
    void testSearchUsers() throws Exception {
        // Arrange
        List<UserResponse> mockResponse = Arrays.asList(
                UserResponse.builder()
                        .id("USER123")
                        .nickname("johndoe")
                        .firstName("John")
                        .build()
        );

        when(userUseCase.searchUsers(any(UserSearchRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "nickname": "johndoe",
                    "siteId": "MLA",
                    "offset": 0,
                    "limit": 50
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("USER123"))
                .andExpect(jsonPath("$[0].nickname").value("johndoe"));
    }
}
