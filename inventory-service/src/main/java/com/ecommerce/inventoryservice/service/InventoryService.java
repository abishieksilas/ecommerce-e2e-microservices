package com.ecommerce.inventoryservice.service;

import com.ecommerce.common.dto.InventoryRequest;
import com.ecommerce.common.dto.InventoryResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stock check and update logic.
 * Order Service calls checkStock via Feign before placing an order.
 */
@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Create or overwrite stock quantity for a product.
     */
    public InventoryResponse updateStock(InventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElse(Inventory.builder().productId(request.getProductId()).quantity(0).build());
        inventory.setQuantity(request.getQuantity());
        Inventory saved = inventoryRepository.save(inventory);
        return InventoryResponse.builder()
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .inStock(saved.getQuantity() > 0)
                .build();
    }

    /**
     * Returns whether enough stock exists for the requested quantity.
     */
    @Transactional(readOnly = true)
    public InventoryResponse checkStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for productId: " + productId));
        boolean inStock = inventory.getQuantity() >= quantity;
        return InventoryResponse.builder()
                .productId(productId)
                .quantity(inventory.getQuantity())
                .inStock(inStock)
                .build();
    }

    /**
     * Decreases stock after a successful order. Fails if not enough quantity.
     */
    public InventoryResponse decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for productId: " + productId));
        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("Insufficient stock for productId: " + productId);
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        Inventory saved = inventoryRepository.save(inventory);
        return InventoryResponse.builder()
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .inStock(saved.getQuantity() > 0)
                .build();
    }

    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for productId: " + productId));
        return InventoryResponse.builder()
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .inStock(inventory.getQuantity() > 0)
                .build();
    }
}
