package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.SearchApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchApiAdapter implements SearchApiPort {

    @Override
    public List<ProductResponse> searchProducts(SearchCriteria criteria) {
        // Mock implementation for testing
        return new ArrayList<>();
    }
}
