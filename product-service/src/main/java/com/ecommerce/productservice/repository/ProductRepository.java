package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository.
 *
 * Extending JpaRepository gives CRUD methods for free:
 * save, findById, findAll, deleteById, etc.
 * No implementation class is needed — Spring generates a proxy at runtime.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
