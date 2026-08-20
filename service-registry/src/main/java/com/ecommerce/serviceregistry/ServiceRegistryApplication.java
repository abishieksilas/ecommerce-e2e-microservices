package com.ecommerce.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server entry point.
 *
 * @EnableEurekaServer turns this Spring Boot app into a service registry.
 *
 * WHY EUREKA IS REQUIRED
 * ----------------------
 * In microservices, instances come and go (restarts, scaling, new machines).
 * Hardcoding host:port in every client is fragile.
 * Eureka keeps a live registry of who is up and where they live.
 *
 * HOW REGISTRATION WORKS
 * ----------------------
 * 1. A service starts with spring.application.name=product-service
 * 2. Eureka Client sends a heartbeat/register request to this server
 * 3. Eureka stores: service name, IP, port, health status
 * 4. Client renews lease with heartbeats; if heartbeats stop, instance is evicted
 *
 * HOW SERVICE DISCOVERY WORKS
 * ---------------------------
 * 1. Caller asks Eureka: "Where is INVENTORY-SERVICE?"
 * 2. Eureka returns one or more healthy instance URLs
 * 3. Feign / Gateway pick an instance and call it
 * 4. No hardcoded inventory host is needed
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }
}
