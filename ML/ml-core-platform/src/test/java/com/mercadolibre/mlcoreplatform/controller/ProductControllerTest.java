package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.ProductUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.ProductController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;
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
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductUseCase productUseCase;

    private ProductController productController;

    @BeforeEach
    void setUp() {
        productController = new ProductController(productUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Arrange
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("ITEM123")
                        .title("iPhone 14")
                        .price(BigDecimal.valueOf(999.99))
                        .categoryId("MLA1055")
                        .build(),
                ProductResponse.builder()
                        .id("ITEM456")
                        .title("Samsung Galaxy S23")
                        .price(BigDecimal.valueOf(799.99))
                        .categoryId("MLA1055")
                        .build()
        );

        when(productUseCase.getAllProducts(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/items")
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("ITEM123"))
                .andExpect(jsonPath("$[0].title").value("iPhone 14"))
                .andExpect(jsonPath("$[1].id").value("ITEM456"))
                .andExpect(jsonPath("$[1].title").value("Samsung Galaxy S23"));
    }

    @Test
    void testGetAllProductsWithDefaultParams() throws Exception {
        // Arrange
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("ITEM123")
                        .title("iPhone 14")
                        .build()
        );

        when(productUseCase.getAllProducts(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetProductById() throws Exception {
        // Arrange
        String productId = "ITEM123";
        ProductResponse mockResponse = ProductResponse.builder()
                .id(productId)
                .title("iPhone 14")
                .price(BigDecimal.valueOf(999.99))
                .categoryId("MLA1055")
                .condition("new")
                .currencyId("ARS")
                .build();

        when(productUseCase.getProductById(productId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/items/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.title").value("iPhone 14"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.categoryId").value("MLA1055"))
                .andExpect(jsonPath("$.condition").value("new"));
    }

    @Test
    void testGetProductDescription() throws Exception {
        // Arrange
        String productId = "ITEM123";
        ProductDescriptionResponse mockResponse = ProductDescriptionResponse.builder()
                .text("Descripción detallada del iPhone 14")
                .plainText("iPhone 14 con 128GB de almacenamiento")
                .build();

        when(productUseCase.getProductDescription(productId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/items/{id}/description", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Descripción detallada del iPhone 14"))
                .andExpect(jsonPath("$.plainText").value("iPhone 14 con 128GB de almacenamiento"));
    }

    @Test
    void testGetShippingOptions() throws Exception {
        // Arrange
        String productId = "ITEM123";
        ShippingOptionsResponse mockResponse = ShippingOptionsResponse.builder()
                .itemId(productId)
                .options(Arrays.asList(
                        ShippingOptionsResponse.ShippingOption.builder()
                                .id("option1")
                                .name("Envío gratis")
                                .mode("me2")
                                .cost(BigDecimal.ZERO)
                                .freeShipping(true)
                                .build()
                ))
                .build();

        when(productUseCase.getShippingOptions(productId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/items/{id}/shipping_options", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value(productId))
                .andExpect(jsonPath("$.options").isArray())
                .andExpect(jsonPath("$.options[0].mode").value("me2"))
                .andExpect(jsonPath("$.options[0].freeShipping").value(true));
    }

    @Test
    void testSearchProducts() throws Exception {
        // Arrange
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("ITEM123")
                        .title("iPhone 14")
                        .price(BigDecimal.valueOf(999.99))
                        .build(),
                ProductResponse.builder()
                        .id("ITEM456")
                        .title("iPhone 13")
                        .price(BigDecimal.valueOf(699.99))
                        .build()
        );

        when(productUseCase.searchProducts(any(ProductSearchRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "query": "iPhone",
                    "categoryId": "MLA1055",
                    "minPrice": 500,
                    "maxPrice": 1500,
                    "condition": "new",
                    "offset": 0,
                    "limit": 50
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("ITEM123"))
                .andExpect(jsonPath("$[0].title").value("iPhone 14"))
                .andExpect(jsonPath("$[1].id").value("ITEM456"))
                .andExpect(jsonPath("$[1].title").value("iPhone 13"));
    }
}
