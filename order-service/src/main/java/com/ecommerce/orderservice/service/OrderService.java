package com.ecommerce.orderservice.service;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.InventoryResponse;
import com.ecommerce.common.dto.OrderRequest;
import com.ecommerce.common.dto.OrderResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.client.InventoryClient;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Order business logic.
 * Always checks inventory via Feign before saving an order.
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        // 1) Ask Inventory Service if stock is available
        ApiResponse<InventoryResponse> stockResponse =
                inventoryClient.checkStock(request.getProductId(), request.getQuantity());

        if (stockResponse == null || stockResponse.getData() == null) {
            throw new BusinessException("Unable to verify stock with inventory-service");
        }

        InventoryResponse stock = stockResponse.getData();
        if (!stock.isInStock()) {
            throw new BusinessException(
                    "Insufficient stock for productId: " + request.getProductId()
                            + ". Available: " + stock.getQuantity());
        }

        // 2) Reserve/consume stock
        inventoryClient.decreaseStock(request.getProductId(), request.getQuantity());

        // 3) Persist order locally in order_db
        BigDecimal total = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(total)
                .status("CREATED")
                .build();

        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
