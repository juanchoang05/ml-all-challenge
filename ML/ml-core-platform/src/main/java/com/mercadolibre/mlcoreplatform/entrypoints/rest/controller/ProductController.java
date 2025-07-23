package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.ProductUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Products", description = "Endpoints para gestión de productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna lista de productos con paginación")
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<ProductResponse> products = productUseCase.getAllProducts(offset, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna información detallada de un producto")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID del producto") @PathVariable String id) {
        ProductResponse product = productUseCase.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}/description")
    @Operation(summary = "Obtener descripción del producto", description = "Retorna la descripción detallada del producto")
    public ResponseEntity<ProductDescriptionResponse> getProductDescription(
            @Parameter(description = "ID del producto") @PathVariable String id) {
        ProductDescriptionResponse description = productUseCase.getProductDescription(id);
        return ResponseEntity.ok(description);
    }

    @GetMapping("/{id}/shipping_options")
    @Operation(summary = "Obtener opciones de envío", description = "Retorna las opciones de envío disponibles para el producto")
    public ResponseEntity<ShippingOptionsResponse> getShippingOptions(
            @Parameter(description = "ID del producto") @PathVariable String id) {
        ShippingOptionsResponse shippingOptions = productUseCase.getShippingOptions(id);
        return ResponseEntity.ok(shippingOptions);
    }

    @PostMapping("/search")
    @Operation(summary = "Buscar productos", description = "Busca productos según criterios específicos")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestBody ProductSearchRequest request) {
        List<ProductResponse> products = productUseCase.searchProducts(request);
        return ResponseEntity.ok(products);
    }
}
