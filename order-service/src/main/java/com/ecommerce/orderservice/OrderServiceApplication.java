package com.ecommerce.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Order Service entry point.
 *
 * @EnableFeignClients scans for interfaces annotated with @FeignClient
 * and creates runtime HTTP client proxies for them.
 *
 * Place-order flow:
 * Controller -> OrderService -> InventoryClient (Feign) -> Inventory Service
 *            -> if inStock -> save order + decrease stock
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
