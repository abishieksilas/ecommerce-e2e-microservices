package com.ecommerce.orderservice.client;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for Inventory Service.
 *
 * HOW FEIGN WORKS INTERNALLY
 * --------------------------
 * 1. @EnableFeignClients triggers component scanning for @FeignClient interfaces.
 * 2. Spring Cloud OpenFeign creates a JDK dynamic proxy for this interface.
 * 3. When you call checkStock(...), the proxy:
 *      a) Builds an HTTP request from the method annotations (@GetMapping, @RequestParam)
 *      b) Resolves "inventory-service" via Eureka + Spring Cloud LoadBalancer
 *      c) Sends the HTTP call (using underlying client, often JDK HttpURLConnection / Apache)
 *      d) Decodes the JSON response into ApiResponse&lt;InventoryResponse&gt;
 * 4. Your service code looks like a normal Java method call — no RestTemplate boilerplate.
 *
 * name = Eureka service id (spring.application.name of inventory-service)
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/check")
    ApiResponse<InventoryResponse> checkStock(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/decrease")
    ApiResponse<InventoryResponse> decreaseStock(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity);
}
