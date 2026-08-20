package com.ecommerce.orderservice.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.OrderRequest;
import com.ecommerce.common.dto.OrderResponse;
import com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order REST APIs.
 *
 * Direct:  http://localhost:8083/orders
 * Gateway: http://localhost:8080/api/orders
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Place and view orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Place an order (checks inventory first via Feign)")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse created = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Order placed successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View order by id")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Order fetched successfully", orderService.getById(id)));
    }
}
