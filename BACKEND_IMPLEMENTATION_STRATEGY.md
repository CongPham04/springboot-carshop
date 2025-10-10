# Auto88 Backend Implementation Strategy (v4)

Date: October 9, 2025

## 1. Objective

This document outlines a clear implementation strategy to align the `oto-shop` backend with the functional requirements specified in `PROJECT_BACKEND_PLAN.md`. It details the necessary data model adjustments, API endpoint creation, and business logic implementation.

---

## 2. Phase 1: Data Model and Schema Synchronization

This phase focuses on correcting and completing the database schema.

### 2.1. Refactor to Use Enums for Brand and Category
- **Directive**: The project must adhere to the original `PROJECT_BACKEND_PLAN.md`, which specifies using `enums` for car brands and categories. The current implementation using `CarBrand` and `CarCategory` entities must be refactored.
- **Action Steps**:
    1.  **Create Enums**: Create `Brand.java` and `Category.java` enums in the `com.carshop.oto_shop.enums` package.
    2.  **Update `Car` Entity**: Modify the `Car.java` entity. Remove the `@ManyToOne` relationships to `CarBrand` and `CarCategory`. Add new fields `private Brand brand;` and `private Category category;` and annotate them with `@Enumerated(EnumType.STRING)`.
    3.  **Database Migration**: Prepare a database migration script to drop the foreign key constraints on the `cars` table, remove the `car_brand_id` and `car_category_id` columns, and add new `brand` and `category` columns (e.g., `VARCHAR(50)`).
    4.  **Remove Redundant Code**: Delete the `CarBrand.java` and `CarCategory.java` entities, repositories, and controllers.
    5.  **Update Service Layer**: Refactor any service logic that relied on the old entities to now use the enums.

### 2.2. Implement Missing Entities
- **Action**: Create the following JPA entities, repositories, and corresponding database tables:
    - `Promotion`: To manage discounts and special offers.
    - `PromotionCar`: A join table to link promotions to specific cars.
    - `News`: To manage blog posts and articles.

### 2.3. Augment Existing Entities
- **Action**: Add required fields and refactor existing fields in the entities as follows.
    - **In `Car.java`**:
        1.  **Create `Color.java` Enum**: Create a new `Color.java` enum in the `enums` package.
        2.  **Refactor `color` field**: Replace the existing `private String color;` with `private Color color;` and annotate it with `@Enumerated(EnumType.STRING)`.
    - **In `Order.java`**:
        - Add the field: `private String cancelReason;`
    - **In `OrderDetail.java`**:
        - Add the field: `private Color colorName;` (This should be an enum to snapshot the color at the time of order).

---

## 3. Phase 2: API Layer Development

This phase involves creating and completing the REST API controllers.

### 3.1. Build User-Facing & Core Feature Controllers
- **Action**: Create the following new controllers:
    - `HomeController`: To serve aggregated data for the main page.
    - `SearchController`: To provide advanced product search and filtering.
    - `CompareController`: To handle side-by-side car comparisons.
    - `MetaController`: To expose master data like brands and categories for UI filters.

### 3.2. Build Content and Admin Controllers
- **Action**: Create controllers for content and administrative functions:
    - `NewsController`: Public endpoints for reading news and admin endpoints for CRUD operations.
    - `PromotionController`: Public endpoints for viewing active promotions and admin endpoints for CRUD.
    - `AdminDashboardController`: Endpoints to provide statistics and reports for the admin panel.

### 3.3. Complete Existing Controllers
- **Action**: Add missing endpoints to existing controllers:
    - **`CarController`**: Add `ADMIN`-role-protected `POST`, `PUT`, `DELETE` methods.
    - **`OrderController`**: Add the user-facing order cancellation endpoint and admin-facing status management endpoints.
    - **`PaymentController`**: Add endpoints to simulate payment confirmation and failure, which will trigger updates to the order status.

---

## 4. Phase 3: Business Logic Implementation

This phase focuses on implementing the core operational logic in the service layer.

### 4.1. Implement Promotion Logic
- **Location**: `CarService` and a new `PromotionService`.
- **Task**: Develop the mechanism to calculate a car's final price by applying the most advantageous active promotion.

### 4.2. Implement Inventory Control
- **Location**: `OrderService`.
- **Task**: Ensure that car stock levels are only decremented when an order is `CONFIRMED` and restored if a confirmed order is cancelled.

### 4.3. Implement Order State Machine
- **Location**: `OrderService`.
- **Task**: Enforce the correct lifecycle for orders (e.g., `PENDING` -> `CONFIRMED` -> `SHIPPING`), preventing invalid state transitions.

---

## 5. Execution Plan

1.  **Execute Phase 1**: Begin by applying all database schema changes and updating JPA entities. This is the foundation for all subsequent work.
2.  **Execute Phase 2**: Develop the API layer, starting with the user-facing controllers (`Home`, `Search`) to facilitate frontend development.
3.  **Execute Phase 3**: Implement the service-layer business logic, ensuring all rules are correctly applied.
4.  **Testing**: Continuously write unit and integration tests throughout all phases to ensure correctness and prevent regressions.
