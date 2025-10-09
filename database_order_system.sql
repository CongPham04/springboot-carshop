-- =====================================================
-- Order Management System - Database Schema
-- Generated: 2025-10-08
-- Tables: orders, order_details, payment
-- =====================================================

USE car_sales_db;

-- =====================================================
-- Table: orders
-- Description: Main order table with shipping info
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,

    -- Shipping information (snapshot at order time)
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50),
    district VARCHAR(50),
    ward VARCHAR(50),
    note TEXT,

    -- Financial breakdown
    subtotal DECIMAL(15,2) NOT NULL,
    shipping_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(15,2) NOT NULL,

    -- Order tracking
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPING', 'DELIVERED', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'PENDING',

    -- Audit timestamps
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_orders_user_id (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: order_details
-- Description: Line items for each order
-- =====================================================
CREATE TABLE IF NOT EXISTS order_details (
    order_detail_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(36) NOT NULL,
    car_id BIGINT NOT NULL,

    -- Order item details
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(15,2) NOT NULL COMMENT 'Price at time of order',

    -- Foreign Keys
    CONSTRAINT fk_order_details_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_details_car FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE RESTRICT,

    -- Indexes
    INDEX idx_order_details_order_id (order_id),
    INDEX idx_order_details_car_id (car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: payment
-- Description: Payment records for orders (1:1)
-- =====================================================
CREATE TABLE IF NOT EXISTS payment (
    payment_id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL UNIQUE COMMENT 'One payment per order',

    -- Payment details
    payment_date DATETIME,
    amount DECIMAL(15,2) NOT NULL,
    payment_method ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'VNPAY', 'MOMO') NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'PENDING',

    -- External payment reference
    transaction_id VARCHAR(255),

    -- Audit timestamps
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,

    -- Indexes
    INDEX idx_payment_order_id (order_id),
    INDEX idx_payment_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Sample Data (Optional - for testing)
-- =====================================================
-- Uncomment to insert sample data:
/*
-- Insert sample order
INSERT INTO orders (order_id, user_id, full_name, email, phone, address, city, district, ward, subtotal, shipping_fee, tax, total_amount, status)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'user-id-here', 'John Doe', 'john@example.com', '0123456789', '123 Main St', 'Hanoi', 'Ba Dinh', 'Cong Vi', 500000000.00, 50000.00, 25000000.00, 525050000.00, 'PENDING');

-- Insert order detail
INSERT INTO order_details (order_id, car_id, quantity, price)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 123456, 1, 500000000.00);

-- Insert payment
INSERT INTO payment (payment_id, order_id, amount, payment_method, status)
VALUES ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', 525050000.00, 'BANK_TRANSFER', 'PENDING');
*/

-- =====================================================
-- Verification Queries
-- =====================================================
-- Check table structure
DESCRIBE orders;
DESCRIBE order_details;
DESCRIBE payment;

-- Check constraints
SELECT
    TABLE_NAME,
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE
FROM
    INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE
    TABLE_SCHEMA = 'car_sales_db'
    AND TABLE_NAME IN ('orders', 'order_details', 'payment');
