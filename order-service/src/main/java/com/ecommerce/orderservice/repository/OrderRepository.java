package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Order persistence — standard Spring Data JPA CRUD.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
