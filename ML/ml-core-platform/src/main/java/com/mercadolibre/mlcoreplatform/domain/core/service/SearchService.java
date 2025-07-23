package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.domain.port.in.SearchUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.domain.port.out.SearchApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase {
    
    private final SearchApiPort searchApiPort;
    
    @Override
    public List<ProductResponse> searchProducts(SearchCriteria criteria) {
        return searchApiPort.searchProducts(criteria);
    }
}
