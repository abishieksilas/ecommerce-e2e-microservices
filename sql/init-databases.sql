-- ============================================================
-- E-Commerce Microservices - MySQL setup
-- Run this once in MySQL Workbench / CLI as root (or your admin user)
-- ============================================================

CREATE DATABASE IF NOT EXISTS product_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS inventory_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS order_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Optional: dedicated app user (recommended for learning beyond root)
-- CREATE USER IF NOT EXISTS 'ecom'@'localhost' IDENTIFIED BY 'ecom';
-- GRANT ALL PRIVILEGES ON product_db.* TO 'ecom'@'localhost';
-- GRANT ALL PRIVILEGES ON inventory_db.* TO 'ecom'@'localhost';
-- GRANT ALL PRIVILEGES ON order_db.* TO 'ecom'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================================
-- Tables below are also auto-created by Hibernate (ddl-auto=update).
-- They are provided so you can understand the schema.
-- ============================================================

USE product_db;

CREATE TABLE IF NOT EXISTS products (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price       DECIMAL(12, 2) NOT NULL
);

USE inventory_db;

CREATE TABLE IF NOT EXISTS inventory (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity   INT NOT NULL,
    CONSTRAINT uk_inventory_product UNIQUE (product_id)
);

USE order_db;

CREATE TABLE IF NOT EXISTS orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL,
    status      VARCHAR(30) NOT NULL,
    created_at  DATETIME NOT NULL
);

-- Sample data (optional)
USE product_db;
INSERT INTO products (name, description, price) VALUES
('Wireless Mouse', 'Ergonomic wireless mouse', 799.00),
('USB-C Hub', '7-in-1 USB-C hub', 2499.00)
ON DUPLICATE KEY UPDATE name = VALUES(name);

USE inventory_db;
INSERT INTO inventory (product_id, quantity) VALUES
(1, 50),
(2, 20)
ON DUPLICATE KEY UPDATE quantity = VALUES(quantity);
