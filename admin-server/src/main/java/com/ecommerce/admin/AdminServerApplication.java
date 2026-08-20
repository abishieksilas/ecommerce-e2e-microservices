package com.ecommerce.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Boot Admin Server entry point.
 *
 * @EnableAdminServer starts the Admin UI.
 * @EnableDiscoveryClient lets Admin find apps registered in Eureka.
 *
 * Open: http://localhost:8090
 *
 * WHAT YOU CAN MONITOR
 * --------------------
 * Health  - UP/DOWN and component details
 * Memory  - JVM heap / non-heap usage
 * CPU     - process CPU load
 * Beans   - Spring beans currently in the context
 * Metrics - counters, timers, gauges from Actuator/Micrometer
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
