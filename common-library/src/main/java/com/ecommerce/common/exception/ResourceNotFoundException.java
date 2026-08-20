package com.ecommerce.common.exception;

/**
 * Thrown when a requested resource (product, order, inventory row) does not exist.
 * Global exception handlers map this to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
