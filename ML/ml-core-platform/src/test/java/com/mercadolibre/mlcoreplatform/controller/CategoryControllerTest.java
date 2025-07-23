package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.CategoryUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.CategoryController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryUseCase categoryUseCase;

    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryController = new CategoryController(categoryUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Arrange
        List<CategoryResponse> mockResponse = Arrays.asList(
                CategoryResponse.builder()
                        .id("MLA1055")
                        .name("Celulares y Smartphones")
                        .build(),
                CategoryResponse.builder()
                        .id("MLA1648")
                        .name("Computación")
                        .build()
        );

        when(categoryUseCase.getAllCategories()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("MLA1055"))
                .andExpect(jsonPath("$[0].name").value("Celulares y Smartphones"))
                .andExpect(jsonPath("$[1].id").value("MLA1648"))
                .andExpect(jsonPath("$[1].name").value("Computación"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        // Arrange
        String categoryId = "MLA1055";
        CategoryResponse mockResponse = CategoryResponse.builder()
                .id(categoryId)
                .name("Celulares y Smartphones")
                .build();

        when(categoryUseCase.getCategoryById(categoryId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Celulares y Smartphones"));
    }

    @Test
    void testGetCategoryAttributes() throws Exception {
        // Arrange
        String categoryId = "MLA1055";
        List<CategoryResponse.AttributeResponse> mockResponse = Arrays.asList(
                CategoryResponse.AttributeResponse.builder()
                        .id("BRAND")
                        .name("Marca")
                        .build(),
                CategoryResponse.AttributeResponse.builder()
                        .id("MODEL")
                        .name("Modelo")
                        .build()
        );

        when(categoryUseCase.getCategoryAttributes(categoryId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/categories/{id}/attributes", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("BRAND"))
                .andExpect(jsonPath("$[0].name").value("Marca"))
                .andExpect(jsonPath("$[1].id").value("MODEL"))
                .andExpect(jsonPath("$[1].name").value("Modelo"));
    }

    @Test
    void testGetCategoriesBySite() throws Exception {
        // Arrange
        String siteId = "MLA";
        List<CategoryResponse> mockResponse = Arrays.asList(
                CategoryResponse.builder()
                        .id("MLA1055")
                        .name("Celulares y Smartphones")
                        .build(),
                CategoryResponse.builder()
                        .id("MLA1648")
                        .name("Computación")
                        .build()
        );

        when(categoryUseCase.getCategoriesBySite(siteId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/categories/sites/{siteId}/categories", siteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("MLA1055"))
                .andExpect(jsonPath("$[1].id").value("MLA1648"));
    }

    @Test
    void testPredictCategory() throws Exception {
        // Arrange
        String siteId = "MLA";
        String title = "iPhone 14 128GB";
        CategoryResponse mockResponse = CategoryResponse.builder()
                .id("MLA1055")
                .name("Celulares y Smartphones")
                .build();

        when(categoryUseCase.predictCategory(siteId, title)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/categories/sites/{siteId}/category_predictor/predict", siteId)
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("MLA1055"))
                .andExpect(jsonPath("$.name").value("Celulares y Smartphones"));
    }
}
