package com.ecommerce.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server entry point.
 *
 * @EnableConfigServer exposes HTTP endpoints such as:
 *   GET /{application}/{profile}
 *   GET /{application}-{profile}.yml
 *
 * Example:
 *   http://localhost:8888/product-service/default
 * returns the merged config for product-service.
 *
 * HOW IT WORKS
 * ------------
 * 1. Config files live in a Git repo (here: config-repo/)
 * 2. File naming: {spring.application.name}.yml  e.g. product-service.yml
 * 3. On startup, each microservice uses bootstrap/config-import to pull remote config
 * 4. Remote properties override (or merge with) local application.yml
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
