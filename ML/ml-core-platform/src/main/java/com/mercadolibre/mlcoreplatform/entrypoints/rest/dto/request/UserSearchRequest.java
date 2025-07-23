package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request;

import lombok.Builder;

@Builder
public record UserSearchRequest(
        String siteId,
        String nickname,
        String email,
        Integer limit,
        Integer offset
) {
}
