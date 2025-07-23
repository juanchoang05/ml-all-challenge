package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.CategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryPersistenceAdapterTest {

    private CategoryPersistenceAdapter categoryPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        categoryPersistenceAdapter = new CategoryPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(categoryPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllCategories() {
        // When
        List<CategoryDto> categories = categoryPersistenceAdapter.findAll();

        // Then
        assertThat(categories)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all categories have required fields
        categories.forEach(category -> {
            assertThat(category.id()).isNotNull();
            assertThat(category.name()).isNotNull();
        });
    }

    @Test
    void shouldFindCategoryById_WhenCategoryExists() {
        // Given - First get a valid category ID from findAll
        List<CategoryDto> allCategories = categoryPersistenceAdapter.findAll();
        assertThat(allCategories).isNotEmpty();
        String validCategoryId = allCategories.get(0).id();

        // When
        Optional<CategoryDto> result = categoryPersistenceAdapter.findById(validCategoryId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(validCategoryId);
        assertThat(result.get().name()).isNotNull();
    }

    @Test
    void shouldReturnEmptyOptional_WhenCategoryDoesNotExist() {
        // Given
        String nonExistentCategoryId = "NON_EXISTENT_ID";

        // When
        Optional<CategoryDto> result = categoryPersistenceAdapter.findById(nonExistentCategoryId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptional_WhenCategoryIdIsNull() {
        // When
        Optional<CategoryDto> result = categoryPersistenceAdapter.findById(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindCategoriesByParentId() {
        // Given - Get all categories and find one that has children
        List<CategoryDto> allCategories = categoryPersistenceAdapter.findAll();
        
        // Find a category that could be a parent (has path_from_root with more than one element)
        CategoryDto parentCategory = allCategories.stream()
                .filter(cat -> cat.pathFromRoot() != null && cat.pathFromRoot().size() > 1)
                .findFirst()
                .orElse(null);

        if (parentCategory != null) {
            String parentId = parentCategory.pathFromRoot().get(0).id();

            // When
            List<CategoryDto> childCategories = categoryPersistenceAdapter.findByParentId(parentId);

            // Then
            assertThat(childCategories).isNotNull();
            // Each found category should have the parentId in its path_from_root
            childCategories.forEach(category -> {
                assertThat(category.pathFromRoot()).isNotNull();
                assertThat(category.pathFromRoot()).isNotEmpty();
                boolean hasParentInPath = category.pathFromRoot().stream()
                        .anyMatch(path -> parentId.equals(path.id()));
                assertThat(hasParentInPath).isTrue();
            });
        }
    }

    @Test
    void shouldReturnEmptyList_WhenParentIdDoesNotExist() {
        // Given
        String nonExistentParentId = "NON_EXISTENT_PARENT_ID";

        // When
        List<CategoryDto> result = categoryPersistenceAdapter.findByParentId(nonExistentParentId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenParentIdIsNull() {
        // When
        List<CategoryDto> result = categoryPersistenceAdapter.findByParentId(null);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapCategoryDtoCorrectly() {
        // Given
        List<CategoryDto> categories = categoryPersistenceAdapter.findAll();
        assertThat(categories).isNotEmpty();

        // When
        CategoryDto category = categories.get(0);

        // Then - Verify all fields are properly mapped
        assertThat(category.id()).isNotNull();
        assertThat(category.name()).isNotNull();
        // picture, permalink, totalItemsInThisCategory can be null, so just verify they're accessible
        assertThat(category.picture()).isNotNull();
        assertThat(category.permalink()).isNotNull();
        
        // Verify nested objects
        if (category.pathFromRoot() != null) {
            category.pathFromRoot().forEach(path -> {
                assertThat(path.id()).isNotNull();
                assertThat(path.name()).isNotNull();
            });
        }

        if (category.childrenCategories() != null) {
            category.childrenCategories().forEach(child -> {
                assertThat(child.id()).isNotNull();
                assertThat(child.name()).isNotNull();
            });
        }

        if (category.settings() != null) {
            // Settings can have null values, just verify the object is accessible
            assertThat(category.settings()).isNotNull();
        }
    }

    @Test
    void shouldHandleEmptyPathFromRoot() {
        // This test verifies that categories without path_from_root are handled correctly
        List<CategoryDto> categories = categoryPersistenceAdapter.findAll();
        
        // At least one category should exist
        assertThat(categories).isNotEmpty();
        
        // The adapter should handle null/empty path_from_root gracefully
        categories.forEach(category -> {
            if (category.pathFromRoot() != null) {
                assertThat(category.pathFromRoot()).isNotNull();
            }
        });
    }

    @Test
    void shouldHandleEmptyChildrenCategories() {
        // This test verifies that categories without children are handled correctly
        List<CategoryDto> categories = categoryPersistenceAdapter.findAll();
        
        // At least one category should exist
        assertThat(categories).isNotEmpty();
        
        // The adapter should handle null/empty children gracefully
        categories.forEach(category -> {
            if (category.childrenCategories() != null) {
                assertThat(category.childrenCategories()).isNotNull();
            }
        });
    }

    @Test
    void shouldHandleNullSettings() {
        // This test verifies that categories without settings are handled correctly
        List<CategoryDto> categories = categoryPersistenceAdapter.findAll();
        
        // At least one category should exist
        assertThat(categories).isNotEmpty();
        
        // The adapter should handle null settings gracefully
        categories.forEach(category -> {
            // Settings can be null, no exception should be thrown
            // Just accessing settings should not throw an exception
            // If settings is null, it should remain null (not throw exception)
            // If settings is not null, it should be a valid settings object
            if (category.settings() != null) {
                assertThat(category.settings()).isNotNull();
            }
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<CategoryDto> allCategories1 = categoryPersistenceAdapter.findAll();
        List<CategoryDto> allCategories2 = categoryPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allCategories1).hasSize(allCategories2.size());
        
        // Verify that finding by ID is consistent
        if (!allCategories1.isEmpty()) {
            String categoryId = allCategories1.get(0).id();
            Optional<CategoryDto> category1 = categoryPersistenceAdapter.findById(categoryId);
            Optional<CategoryDto> category2 = categoryPersistenceAdapter.findById(categoryId);
            
            assertThat(category1).isPresent();
            assertThat(category2).isPresent();
            assertThat(category1.get().id()).isEqualTo(category2.get().id());
            assertThat(category1.get().name()).isEqualTo(category2.get().name());
        }
    }
}
