package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway entry point.
 *
 * Spring Cloud Gateway (WebFlux-based) replaces the old Zuul gateway.
 * Routes are defined in Config Server (api-gateway.yml).
 *
 * REQUEST FLOW EXAMPLE (Place Order)
 * ----------------------------------
 * Client -> http://localhost:8080/api/orders
 *        -> Gateway matches Path=/api/orders/**
 *        -> RewritePath to /orders
 *        -> Eureka lookup for order-service
 *        -> Forward to http://<order-host>:8083/orders
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
