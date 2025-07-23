package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;

import java.util.List;

public interface SearchApiPort {
    
    List<ProductResponse> searchProducts(SearchCriteria criteria);
}
