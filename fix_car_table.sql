-- Fix Car table: Remove old foreign key columns and constraints
-- Run this SQL in MySQL Workbench or command line

USE car_sales_db;

-- Drop foreign key constraints first
ALTER TABLE cars DROP FOREIGN KEY IF EXISTS fk_cars_brand;
ALTER TABLE cars DROP FOREIGN KEY IF EXISTS fk_cars_category;

-- Drop old columns
ALTER TABLE cars DROP COLUMN IF EXISTS brand_id;
ALTER TABLE cars DROP COLUMN IF EXISTS category_id;

-- Verify the table structure
DESCRIBE cars;
