package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.SearchUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.SearchController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
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
class SearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchUseCase searchUseCase;

    private SearchController searchController;

    @BeforeEach
    void setUp() {
        searchController = new SearchController(searchUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    void testSearchProducts() throws Exception {
        // Arrange
        String siteId = "MLA";
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

        when(searchUseCase.searchProducts(any(SearchCriteria.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/sites/{siteId}/search", siteId)
                        .param("q", "smartphone")
                        .param("categoryId", "MLA1055")
                        .param("condition", "new")
                        .param("minPrice", "500")
                        .param("maxPrice", "1500")
                        .param("shipping", "free")
                        .param("sort", "price_asc")
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
    void testSearchProductsWithMinimalParams() throws Exception {
        // Arrange
        String siteId = "MLA";
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("ITEM123")
                        .title("iPhone 14")
                        .build()
        );

        when(searchUseCase.searchProducts(any(SearchCriteria.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/sites/{siteId}/search", siteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("ITEM123"));
    }

    @Test
    void testSearchProductsWithQuery() throws Exception {
        // Arrange
        String siteId = "MLA";
        String query = "laptop gaming";
        List<ProductResponse> mockResponse = Arrays.asList(
                ProductResponse.builder()
                        .id("LAPTOP123")
                        .title("Laptop Gaming MSI")
                        .price(BigDecimal.valueOf(1299.99))
                        .build()
        );

        when(searchUseCase.searchProducts(any(SearchCriteria.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/sites/{siteId}/search", siteId)
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("LAPTOP123"))
                .andExpect(jsonPath("$[0].title").value("Laptop Gaming MSI"));
    }

    @Test
    void testGetTrends() throws Exception {
        // Arrange
        String siteId = "MLA";

        // Act & Assert
        mockMvc.perform(get("/api/sites/{siteId}/search/trends", siteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetSearchSuggestions() throws Exception {
        // Arrange
        String siteId = "MLA";
        String query = "iph";

        // Act & Assert
        mockMvc.perform(get("/api/sites/{siteId}/search/suggestions", siteId)
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}
