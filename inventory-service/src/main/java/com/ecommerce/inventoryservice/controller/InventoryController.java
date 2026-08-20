package com.ecommerce.inventoryservice.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.InventoryRequest;
import com.ecommerce.common.dto.InventoryResponse;
import com.ecommerce.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory REST APIs.
 *
 * Direct:  http://localhost:8082/inventory/...
 * Gateway: http://localhost:8080/api/inventory/...
 *
 * Feign (from order-service) calls /inventory/check and /inventory/decrease.
 */
@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory", description = "Stock check and update APIs")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping
    @Operation(summary = "Create or update stock for a product")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateStock(
            @Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Stock updated successfully", inventoryService.updateStock(request)));
    }

    @GetMapping("/check")
    @Operation(summary = "Check if enough stock is available")
    public ResponseEntity<ApiResponse<InventoryResponse>> checkStock(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(ApiResponse.ok("Stock checked successfully",
                inventoryService.checkStock(productId, quantity)));
    }

    @PostMapping("/decrease")
    @Operation(summary = "Decrease stock after placing an order")
    public ResponseEntity<ApiResponse<InventoryResponse>> decreaseStock(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(ApiResponse.ok("Stock decreased successfully",
                inventoryService.decreaseStock(productId, quantity)));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get current stock by product id")
    public ResponseEntity<ApiResponse<InventoryResponse>> getByProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(ApiResponse.ok("Inventory fetched successfully",
                inventoryService.getByProductId(productId)));
    }
}
