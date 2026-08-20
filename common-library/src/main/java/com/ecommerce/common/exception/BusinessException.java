package com.ecommerce.common.exception;

/**
 * Thrown for business rule violations (e.g. insufficient stock).
 * Global exception handlers map this to HTTP 400.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
