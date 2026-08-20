package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for inventory checks / stock updates.
 * "inStock" is true when available quantity >= requested quantity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long productId;
    private Integer quantity;
    private boolean inStock;
}
