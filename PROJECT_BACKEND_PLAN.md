# Auto88 Car E‑Commerce – Backend Technical Plan (v1)

Analysis Date: October 9, 2025
Project: oto-shop (Auto88 Car Shop API)
Base URL: http://localhost:8080/carshop

---

## 📋 Table of Contents

1. Objectives & Scope
2. Gap Analysis vs Requirements
3. Architecture & Technology
4. Data Model (Proposed Schema)
5. API Design (Public & Admin)
6. Security & RBAC
7. Business Rules & Workflows
8. Performance, Scalability, and Reliability
9. Backups & Disaster Recovery
10. Observability & Auditing
11. Testing Strategy
12. Migration Plan (from current code)
13. Milestones & Deliverables

---

## 1) 🎯 Objectives & Scope

Build a backend that fully satisfies the Auto88 e-commerce requirements with focus on:
- Robust catalog (brands, categories, colors), search, filtering, and comparison
- Product details with variant color selection and inventory
- Cart, ordering, and payments (COD, Bank Transfer, Credit/Debit Card)
- Customer order history and cancellation rules
- News/Blog and Promotions modules
- Admin dashboard for managing catalog, promotions, users, and orders, with sales/stock insights
- Security, performance (≤ 3s avg response), and data protection (backups)

Note: Aligns with Spring Boot 3.x, Java 17, MySQL, JWT—preserving existing strengths.

---

## 2) ✅ Gap Analysis vs Requirements

Current state (see PROJECT_ANALYSIS.md) already covers:
- Auth with JWT, user/account consolidation, RBAC
- Core catalog (cars + details), orders, payments

Gaps/new work required:
- FR4–7 Homepage sections (featured brands, categories, new arrivals, special offers, news)
- FR8–10 Search & filtering (keyword, price range, type/category, color, production year) + stock status
- FR11–12 Car comparison (2–3 cars with detailed specs)
- FR13–15 Product detail with color selection and quantity; Buy Now only (no cart per constraints)
- FR16–22 Customer order management (history, tracking, cancellation if not confirmed)
- FR23–24 News & Blog module (public read, admin CRUD)
- FR25–30 Admin dashboard: manage promotions, news, users, and orders, plus sales/stock statistics (brand/category/color are fixed enums)
- NFR1–NFR6 Responsiveness (backend support), 5k daily visits, ≤3s response, password encryption (already), RBAC (already), backups

Key model notes:
- Brand/Category/Color will remain enums (no master data tables)
- Promotions and News schema needed
- No Cart (intentionally omitted per constraints)

---

## 3) 🏗️ Architecture & Technology

- Framework: Spring Boot 3.5.x, Java 17
- Database: MySQL 8
- ORM: Spring Data JPA (Hibernate)
- Auth: JWT (BCrypt password hashing)
- Docs: springdoc-openapi + Swagger UI
- Optional Caching: Redis (for homepage, lookups, hot searches)
- Build: Maven

Service boundaries (packages):
- auth, users, catalog (brands, categories, colors, cars, car-details), search, compare, promotions, news, cart, orders, payments, dashboard, common (config, exceptions, response), security

---

## 4) 🗄️ Data Model (Proposed Schema)

Note: Per constraints, Brand/Category/Color remain enums. No master data tables and no car_images table. Images and colors are properties on the car row.

### 4.1 Enums

- Brand: e.g., TOYOTA, HONDA, FORD, MERCEDES, HYUNDAI, VINFAST, ...
- Category: e.g., SUV, SEDAN, PICKUP, HATCHBACK, COUPE, ...
- Color: e.g., RED, BLACK, WHITE, SILVER, BLUE, ...

### 4.2 Catalog

1) cars
- car_id BIGINT PK (auto)
- brand ENUM NOT NULL
- category ENUM NOT NULL
- model VARCHAR(80) NOT NULL
- manufacture_year INT NOT NULL
- base_price DECIMAL(15,2) NOT NULL
- status ENUM('AVAILABLE','OUT_OF_STOCK','SOLD') NOT NULL DEFAULT 'AVAILABLE'
- stock_qty INT NOT NULL DEFAULT 0
- available_colors JSON NULL  (array of Color enum names)
- description TEXT NULL
- main_image_url VARCHAR(255) NULL
- gallery_images JSON NULL     (array of image URLs)
- created_at, updated_at DATETIME

Indexes: (brand), (category), (manufacture_year), (status), (model)

2) car_details (1:1 with cars)
- car_detail_id BIGINT PK (auto)
- car_id BIGINT FK UK NOT NULL
- engine VARCHAR(100) NOT NULL
- horsepower INT NOT NULL
- torque INT NOT NULL
- transmission VARCHAR(50) NOT NULL
- fuel_type VARCHAR(30) NOT NULL
- fuel_consumption VARCHAR(50) NOT NULL
- seats INT NOT NULL
- weight DOUBLE NOT NULL
- dimensions VARCHAR(100) NOT NULL

### 4.3 Promotions & News

3) promotions
- promotion_id BIGINT PK (auto)
- title VARCHAR(150) NOT NULL
- description TEXT NULL
- discount_type ENUM('PERCENT','FIXED') NOT NULL
- discount_value DECIMAL(12,2) NOT NULL
- start_at DATETIME NOT NULL
- end_at DATETIME NOT NULL
- active BOOLEAN NOT NULL DEFAULT TRUE
- applies_to ENUM('CAR','CATEGORY','BRAND','GLOBAL') NOT NULL
- target_categories JSON NULL   (array of Category enum names)
- target_brands JSON NULL       (array of Brand enum names)

4) promotion_cars
- promotion_id BIGINT FK NOT NULL
- car_id BIGINT FK NOT NULL
- PRIMARY KEY (promotion_id, car_id)

5) news
- news_id BIGINT PK (auto)
- title VARCHAR(180) NOT NULL
- slug VARCHAR(200) UK NOT NULL
- excerpt VARCHAR(300) NULL
- content MEDIUMTEXT NOT NULL
- cover_image_url VARCHAR(255) NULL
- status ENUM('DRAFT','PUBLISHED') NOT NULL DEFAULT 'DRAFT'
- published_at DATETIME NULL
- created_at, updated_at DATETIME

### 4.4 Orders & Payments

6) orders
- order_id UUID PK
- user_id UUID FK NOT NULL
- full_name, email, phone, address, city, district, ward, note
- subtotal, shipping_fee, tax, total_amount DECIMAL
- order_date DATETIME
- status ENUM('PENDING','CONFIRMED','SHIPPING','DELIVERED','CANCELLED','COMPLETED')
- cancel_reason VARCHAR(255) NULL
- created_at, updated_at

7) order_details
- order_detail_id BIGINT PK (auto)
- order_id UUID FK NOT NULL
- car_id BIGINT FK NOT NULL
- color_name ENUM NULL (snapshot of Color)
- quantity INT NOT NULL
- price DECIMAL(15,2) NOT NULL (snapshot)

8) payments
- payment_id UUID PK
- order_id UUID FK UK NOT NULL
- payment_date DATETIME NULL
- amount DECIMAL(15,2) NOT NULL
- payment_method ENUM('COD','BANK_TRANSFER','CREDIT_CARD') NOT NULL
- status ENUM('PENDING','SUCCESS','FAILED') NOT NULL DEFAULT 'PENDING'
- transaction_id VARCHAR(255) NULL
- created_at, updated_at

### 4.5 Derived/Computed Fields
- Effective price = base_price minus best applicable promotion at query time
- Stock status:
  - OUT_OF_STOCK if stock_qty == 0
  - AVAILABLE if stock_qty > 0
  - SOLD reserved for fully sold (optional, admin-set) or post-completion for unique VIN inventory

---

## 5) 🌐 API Design

Base: /carshop

### 5.1 Homepage
- GET /api/home/sections
  - categories (top N), featuredBrands (top N), featuredCars, newArrivals, specialOffers, latestNews
- GET /api/home/news
- GET /api/home/promotions

### 5.2 Meta (Enums)
- GET /api/meta/brands – returns list of Brand enum values
- GET /api/meta/categories – returns list of Category enum values
- GET /api/meta/colors – returns list of Color enum values

### 5.3 Cars & Details
- Public
  - GET /api/cars?page&size&sort – include effectivePrice, stockStatus
  - GET /api/cars/{carId}
  - GET /api/cars/{carId}/images – returns gallery_images from car row
  - GET /api/cars/{carId}/colors – returns available_colors from car row
- Admin
  - POST /api/cars (brand, category, model, manufactureYear, basePrice, stockQty, status, availableColors[], description, mainImageFile, galleryImages[])
  - PUT /api/cars/{carId}
  - DELETE /api/cars/{carId}

### 5.4 Search & Filtering
- GET /api/search/cars?keyword=&brand=&category=&color=&priceMin=&priceMax=&yearFrom=&yearTo=&status=&sort=&page=&size=
  - Indexes on (brand), (category), (manufacture_year), (base_price) and FULLTEXT(model, description) if supported
  - Filter by color using JSON_CONTAINS(available_colors, '"COLOR"')

### 5.5 Car Comparison
- GET /api/cars/compare?ids=1,2,3 – returns side-by-side specs: engine, power, torque, transmission, fuel, seats, weight, price (effective), colors

### 5.6 Promotions
- Public
  - GET /api/promotions/active
  - GET /api/promotions/{id}
- Admin
  - POST /api/promotions (title, type, value, startAt, endAt, appliesTo, targetBrands[], targetCategories[], targetCarIds[])
  - PUT /api/promotions/{id}
  - DELETE /api/promotions/{id}

### 5.7 News/Blog
- Public (no auth)
  - GET /api/news?page&size
  - GET /api/news/{slug}
- Admin
  - POST /api/news
  - PUT /api/news/{id}
  - DELETE /api/news/{id}
  - PATCH /api/news/{id}/publish

### 5.8 Orders
- User
  - POST /api/orders – direct checkout (Buy Now) with FR16 info. Creates order with status=PENDING
  - GET /api/orders/me?page&size (FR20)
  - GET /api/orders/{orderId} (FR21)
  - POST /api/orders/{orderId}/cancel – only if status=PENDING (FR22)
- Admin
  - GET /api/orders?page&size&status
  - PATCH /api/orders/{orderId}/status – {status}
  - DELETE /api/orders/{orderId}

### 5.9 Payments (FR18–FR19)
- User/Admin
  - POST /api/payments – {orderId, method} → create record PENDING
  - POST /api/payments/{paymentId}/confirm – success (sets status=SUCCESS, payment_date=now, order.status=CONFIRMED)
  - POST /api/payments/{paymentId}/fail – sets status=FAILED
- Bank Transfer helper
  - GET /api/payments/{orderId}/bank-instructions – returns bank account info and reference code
- COD flow
  - Auto-create payment with method=COD, status=PENDING; Admin marks order CONFIRMED after verification
- Card flow
  - Simulated processor adapter (DEV) → status update endpoint invoked on callback

### 5.10 Admin Dashboard & Reports
- GET /api/admin/stats/overview – totals: revenue (range), orders by status, avg order value, conversion
- GET /api/admin/stats/sales?from&to&granularity=day|week|month
- GET /api/admin/stats/top-products?limit=10
- GET /api/admin/stats/top-brands?limit=10
- GET /api/admin/stats/low-stock?threshold=5

---

## 6) 🔒 Security & RBAC

Roles: CUSTOMER, ADMIN

- Public: auth endpoints, catalog GETs, search, compare, news GETs, homepage
- CUSTOMER: cart, my orders, payments for own orders
- ADMIN: CRUD for brands/categories/colors/cars/car-details/images/promotions/news/users; orders management; dashboard

Password storage: BCrypt (already). JWT with access/refresh rotation. Enforce input validation and standardized error responses.

---

## 7) 🧭 Business Rules & Workflows

- Stock & status
  - status=OUT_OF_STOCK if stock_qty==0 (or sum of car_colors stock==0)
  - Deduct stock on order status=CONFIRMED (not on PENDING) to avoid ghost reservations
- Promotions
  - Choose best applicable promotion (car > category > brand > global precedence)
  - Validate now() ∈ [start_at, end_at] and active=true
- Order lifecycle
  - PENDING → CONFIRMED → SHIPPING → DELIVERED → COMPLETED
  - PENDING → CANCELLED (user-initiated; store cancel_reason)
  - State transition validation in service layer
- Payment lifecycle
  - Create PENDING; upon SUCCESS → set order=CONFIRMED
  - BANK_TRANSFER: provide reference; admin confirms on receipt
  - COD: confirm at fulfillment time
- Cart
  - Unit price snapshot captured when item added; refreshed on checkout to avoid stale pricing, then persisted to order_details

---

## 8) ⚡ Performance, Scalability, and Reliability

- Pagination everywhere for list endpoints
- Indexes: cars(brand_id), cars(category_id), cars(manufacture_year), cars(base_price), cars(status), FULLTEXT(model, description) where available
- Caching (optional):
  - home sections (1–5 min TTL), brands/categories/colors (long TTL), hot searches (query key cache)
- Response time budget (≤3s):
  - 95%-ile queries ≤ 200ms DB time via indexes and limit fields
  - Avoid N+1 with JOIN fetch or projection DTOs
- Connection pool: HikariCP tuned for expected concurrency
- Rate limiting (optional): user-level throttling on write endpoints

---

## 9) 💾 Backups & Disaster Recovery

- Daily logical backups using mysqldump (retain 7–14 days)
- Example (Windows Task Scheduler):
  - Command: mysqldump -u root -p%MYSQL_PWD% car_sales_db > D:\backups\car_sales_db_%%DATE%%.sql
- Test restore quarterly into staging
- Include uploads/ images backup (rsync or robocopy nightly)

---

## 10) 👀 Observability & Auditing

- Structured JSON logs (requestId, userId, path, latency, status)
- Error tracking with correlation IDs
- Audit log (optional table) for admin changes on catalog, promotions, orders

---

## 11) 🧪 Testing Strategy

- Unit tests for services (promotion application, state transitions)
- Integration tests for search filters, order creation, payment confirmation
- Contract tests for public APIs (OpenAPI validation)
- Seed data for brands/categories/colors and a few cars for CI

---

## 12) 🔁 Migration Plan (from current code)

Current: Car uses enums for Brand/Category (and likely Color). Keep enums as-is per constraints.

Steps:
1) Cars table changes:
   - Add available_colors JSON (nullable) to store list of Color enum strings
   - Add gallery_images JSON (nullable) to store list of image URLs
   - Ensure main_image_url exists; keep stock_qty/status columns
2) Orders/Order details:
   - Add order_details.color_name as ENUM (Color) for snapshot if not present
   - Add orders.cancel_reason (nullable)
3) Promotions & News:
   - Create promotions table (with target_brands JSON, target_categories JSON)
   - Create promotion_cars join table
   - Create news table
4) Remove/skip:
   - Do not create brands/categories/colors master tables (enums only)
   - Do not create car_images table
   - Do not create carts/cart_items tables
5) Application code updates:
   - DTOs/services/controllers to read/write availableColors and galleryImages on Car
   - SearchController to filter by brand/category enums and JSON_CONTAINS for color
   - Replace any references to FK brand/category with enums
6) Provide SQL migrations for added columns/tables and safe rollbacks

Backward compatibility: expose new fields in responses; legacy enum usage remains intact.

---

## 13) 📆 Milestones & Deliverables

- M1: Schema & migrations (brands/categories/colors/cars refactor, indexes) – 2–3 days
- M2: Catalog APIs + Search/Compare + Homepage sections – 3–4 days
- M3: Promotions & News modules – 2–3 days
- M4: Cart + Order updates (cancel, state machine, stock handling) – 3–4 days
- M5: Payments refinements (COD/Bank/Card flow) – 2 days
- M6: Admin Dashboard stats & reports – 2–3 days
- M7: Observability, Backups, Tests – 2–3 days

Deliverable: OpenAPI updated, test data, migration scripts, admin seeds, and README updates for new modules.

---

Status: Proposed. Requires implementation and incremental rollout with DB migrations.
