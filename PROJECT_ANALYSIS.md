# Auto88 Car Shop - Spring Boot Project Analysis

**Analysis Date:** October 9, 2025
**Project Name:** oto-shop (Auto88 Car Shop API)
**Version:** 0.0.1-SNAPSHOT
**Base URL:** http://localhost:8080/carshop

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Architecture](#project-architecture)
4. [Database Schema](#database-schema)
5. [Security Configuration](#security-configuration)
6. [API Endpoints](#api-endpoints)
7. [Key Features](#key-features)
8. [Configuration](#configuration)
9. [Project Structure](#project-structure)

---

## 🎯 Project Overview

Auto88 Car Shop is a **RESTful API** built with **Spring Boot 3.5.5** for managing a car sales system. The application provides comprehensive functionality for car inventory management, user authentication, a complete order management system, and role-based access control.

**Key Characteristics:**
- **Type:** Spring Boot REST API
- **Java Version:** Java 17
- **Build Tool:** Maven
- **Database:** MySQL (car_sales_db)
- **Authentication:** JWT (JSON Web Tokens)
- **API Documentation:** Swagger/OpenAPI 3.1.0
- **ORM:** Spring Data JPA with Hibernate

---

## 🛠️ Technology Stack

### Core Framework & Dependencies

#### Spring Boot Starters (Version 3.5.5)
- **spring-boot-starter-data-jpa** - Database access with JPA/Hibernate
- **spring-boot-starter-security** - Security framework for authentication/authorization
- **spring-boot-starter-validation** - Bean validation
- **spring-boot-starter-web** - RESTful web services
- **spring-boot-starter-test** - Testing support (JUnit, Mockito, etc.)

#### Database
- **mysql-connector-j** - MySQL database connector
- **Hibernate** - ORM with MySQL8Dialect

#### Security & Authentication
- **Spring Security** - Core security framework
- **JWT (jjwt)** - JSON Web Token implementation
  - jjwt-api (0.13.0)
  - jjwt-impl (0.13.0)
  - jjwt-jackson (0.13.0)

#### Documentation
- **springdoc-openapi-starter-webmvc-ui** (2.8.13) - OpenAPI 3 + Swagger UI

#### Utility Libraries
- **Lombok** - Reduce boilerplate code
- **MapStruct** (1.6.3) - Bean mapping
- **Apache Commons Lang3** (3.18.0) - Utility functions

#### Build Configuration
- **Maven Compiler Plugin** - Configured with annotation processors for:
  - MapStruct processor
  - Lombok processor

---

## 🏗️ Project Architecture

### Layered Architecture

The project follows a standard **multi-layered architecture** pattern:

```
┌─────────────────────────────────────┐
│      Controllers (REST Layer)       │  ← HTTP Requests/Responses
├─────────────────────────────────────┤
│      Services (Business Logic)      │  ← Business rules & validation
├─────────────────────────────────────┤
│   Repositories (Data Access Layer)  │  ← Database operations
├─────────────────────────────────────┤
│         Entities (Domain Model)     │  ← JPA entities
└─────────────────────────────────────┘
           ↓
      MySQL Database
```

### Package Structure

```
com.carshop.oto_shop/
├── common/
│   ├── config/              # Application configurations
│   │   ├── ApplicationInitConfig.java
│   │   ├── CorsConfig.java
│   │   ├── JwtSecretChecker.java
│   │   ├── PasswordConfig.java
│   │   ├── SecurityConfig.java
│   │   ├── SwaggerConfig.java
│   │   └── WebConfig.java
│   ├── exceptions/          # Custom exceptions & error handling
│   │   ├── AppException.java
│   │   ├── BadRequestException.java
│   │   ├── DuplicateKeyException.java
│   │   ├── ErrorCode.java
│   │   ├── ErrorResponse.java
│   │   └── GlobalExceptionHandler.java
│   └── response/            # API response wrappers
│       └── ApiResponse.java
├── controllers/             # REST API controllers
│   ├── AccountController.java (⚠️ Deprecated)
│   ├── AuthController.java
│   ├── CarBrandController.java
│   ├── CarCategoryController.java
│   ├── CarController.java (UPDATED - uses Brand/Category enums)
│   ├── CarDetailController.java
│   ├── OrderController.java (NEW)
│   ├── OrderDetailController.java (NEW)
│   ├── PaymentController.java (NEW)
│   └── UserController.java
├── dto/                     # Data Transfer Objects
│   ├── account/
│   ├── auth/
│   ├── car/
│   ├── carbrand/
│   ├── carcategory/
│   ├── cardetail/
│   ├── order/ (NEW)
│   │   ├── OrderRequest.java
│   │   └── OrderResponse.java
│   ├── orderdetail/ (NEW)
│   │   ├── OrderDetailRequest.java
│   │   └── OrderDetailResponse.java
│   ├── payment/ (NEW)
│   │   ├── PaymentRequest.java
│   │   └── PaymentResponse.java
│   └── user/
│       ├── UserAccountRequest.java (NEW - combined account+user)
│       ├── UserUpdateRequest.java (NEW - update account+user)
│       ├── UserRequest.java
│       └── UserResponse.java (UPDATED - includes account fields)
├── entities/               # JPA entities
│   ├── Account.java
│   ├── Car.java (UPDATED - uses Brand/Category enums)
│   ├── CarBrand.java
│   ├── CarCategory.java
│   ├── CarDetail.java
│   ├── Order.java (NEW)
│   ├── OrderDetail.java (NEW)
│   ├── Payment.java (NEW)
│   └── User.java
├── enums/                  # Enumerations
│   ├── AccountStatus.java
│   ├── Brand.java (NEW - Toyota, Hyundai, Mercedes, Vinfast)
│   ├── CarStatus.java
│   ├── Category.java (NEW - SUV, Sedan, Hatchback)
│   ├── Gender.java
│   ├── OrderStatus.java (NEW)
│   ├── PaymentMethod.java (NEW)
│   ├── PaymentStatus.java (NEW)
│   └── Role.java
├── mappers/                # MapStruct mappers
│   ├── AccountMapper.java
│   ├── CarBrandMapper.java
│   ├── CarCategoryMapper.java
│   ├── CarDetailMapper.java
│   ├── CarMapper.java
│   ├── OrderDetailMapper.java (NEW)
│   ├── OrderMapper.java (NEW)
│   ├── PaymentMapper.java (NEW)
│   └── UserMapper.java
├── repositories/           # Spring Data JPA repositories
│   ├── AccountRepository.java
│   ├── CarBrandRepository.java
│   ├── CarCategoryRepository.java
│   ├── CarDetailRepository.java
│   ├── CarRepository.java (UPDATED - uses Brand/Category enums)
│   ├── OrderDetailRepository.java (NEW)
│   ├── OrderRepository.java (NEW)
│   ├── PaymentRepository.java (NEW)
│   └── UserRepository.java
├── security/               # Security components
│   ├── handers/           # Security handlers
│   ├── jwt/               # JWT authentication filter
│   ├── models/            # Security models
│   └── services/          # UserDetailsService
│       └── CustomUserDetailsService
└── services/              # Business logic services
    ├── AccountService.java
    ├── AuthService.java
    ├── CarBrandService.java (UPDATED - removed car deletion logic)
    ├── CarCategoryService.java (UPDATED - removed car deletion logic)
    ├── CarDetailService.java
    ├── CarService.java (UPDATED - uses Brand/Category enums)
    ├── OrderDetailService.java (NEW)
    ├── OrderService.java (NEW)
    ├── PaymentService.java (NEW)
    └── UserService.java
```

---

## 🗄️ Database Schema

### Database Configuration
- **Database Name:** car_sales_db
- **Host:** localhost:3306
- **Username:** root
- **Hibernate DDL:** update (auto-create/update tables)
- **Dialect:** MySQL8Dialect

### Entity Relationship Diagram

```
┌──────────────┐
│   accounts   │
│──────────────│
│ account_id PK│───┐
│ username UK  │   │
│ email UK     │   │ 1:1
│ password     │   │
│ role         │   │
│ status       │   │
│ created_at   │   │
│ updated_at   │   │
└──────────────┘   │
                   ↓
              ┌─────────┐
              │  users  │───────┐
              │─────────│       │
              │ user_id PK │    │ 1:N
              │ account_id FK │ │
              │ full_name │     │
              │ dob       │     │
              │ gender    │     │
              │ phone UK  │     │
              │ address   │     │
              │ avatar_url│     │
              └─────────┘       │
                                ↓
                           ┌────────────┐
                           │   orders   │───┐
                           │────────────│   │
                           │ order_id PK│   │ 1:N
                           │ user_id FK │   │
                           │ full_name  │   │
                           │ email      │   │
                           │ phone      │   │
                           │ address    │   │
                           │ city       │   │
                           │ district   │   │
                           │ ward       │   │
                           │ note       │   │
                           │ subtotal   │   │
                           │ shipping_fee│  │
                           │ tax        │   │
                           │ total_amount│  │
                           │ order_date │   │
                           │ status     │   │
                           │ created_at │   │
                           │ updated_at │   │
                           └────────────┘   │
                                 │          ↓
                           1:1   │     ┌─────────────┐
                                 │     │order_details│
                                 │     │─────────────│
                                 │     │order_detail_id PK│
                                 │     │order_id FK  │
                                 │     │car_id FK    │
                                 │     │quantity     │
                                 │     │price        │
                                 │     └─────────────┘
                                 ↓           ↑
                           ┌────────────┐    │ N:1
                           │  payment   │    │
                           │────────────│    │
                           │payment_id PK│   │
                           │order_id FK UK│  │
                           │payment_date│    │
                           │amount      │    │
                           │payment_method│  │
                           │status      │    │
                           │transaction_id│  │
                           │created_at  │    │
                           │updated_at  │    │
                           └────────────┘    │
                                             │
┌─────────────────┐                         │
│ car_categories  │                         │
│─────────────────│                         │
│ category_id PK  │───┐                     │
│ category_name UK│   │                     │
└─────────────────┘   │                     │
                      │ N:1                 │
┌──────────────┐      │                     │
│  car_brands  │      │                     │
│──────────────│      │                     │
│ brand_id PK  │──┐   │                     │
│ brand_name UK│  │   │                     │
└──────────────┘  │   │                     │
                  │ N:1 (⚠️ Now using enums)│
                  ↓   ↓                     │
              ┌────────────┐                │
              │    cars    │────────────────┘
              │────────────│
              │ car_id PK  │───┐
              │ brand ENUM │   │ 1:1
              │ category ENUM│ │
              │ model       │  │
              │ manufacture_year│
              │ price       │  │
              │ color       │  │
              │ description │  │
              │ status      │  │
              │ image_url   │  │
              └────────────┘  │
                              ↓
                         ┌──────────────┐
                         │ car_details  │
                         │──────────────│
                         │ car_detail_id PK │
                         │ car_id FK UK │
                         │ engine       │
                         │ horsepower   │
                         │ torque       │
                         │ transmission │
                         │ fuel_type    │
                         │ fuel_consumption │
                         │ seats        │
                         │ weight       │
                         │ dimensions   │
                         └──────────────┘
```

### Entity Details

#### 1. accounts (Authentication Accounts)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| account_id | UUID (36) | PK | Auto-generated UUID |
| username | VARCHAR(50) | UK, NOT NULL | Unique username |
| email | VARCHAR(100) | UK, NOT NULL | Unique email |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| role | ENUM | NOT NULL | USER/ADMIN |
| status | ENUM | NOT NULL | ACTIVE/INACTIVE/BANNED |
| create_at | DATETIME | NOT NULL | Auto-set on creation |
| updated_at | DATETIME | NOT NULL | Auto-updated on modification |

**Enums:**
- **Role:** USER, ADMIN
- **AccountStatus:** ACTIVE, INACTIVE, BANNED

**Features:**
- UUID-based primary key (GenerationType.UUID)
- Unique constraints on username and email
- @PrePersist sets default role (USER) and status (ACTIVE)
- @PreUpdate automatically updates timestamp

#### 2. users (User Personal Information)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | UUID (36) | PK | Auto-generated UUID |
| account_id | UUID (36) | FK, NOT NULL | One-to-one with accounts |
| full_name | VARCHAR(100) | NOT NULL | Full name |
| dob | DATE | | Date of birth |
| gender | ENUM | | MALE/FEMALE/OTHER |
| phone | VARCHAR(20) | UK | Unique phone number |
| address | VARCHAR(255) | | User address |
| avatar_url | VARCHAR(255) | | Profile image URL |

**Relationships:**
- One-to-one with Account (EAGER fetch - always loaded with user)
- Foreign key: fk_users_account

**Design Note:**
Account is fetched eagerly (default for @OneToOne) to support the consolidated API response that includes account information in all User endpoints.

#### 3. car_categories (Car Categories)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| category_id | BIGINT | PK | Random 8-digit ID |
| category_name | VARCHAR(100) | UK, NOT NULL | Unique category name |

**Features:**
- Auto-generated random ID (10000000 + random 90000000)
- Examples: SUV, Sedan, Hatchback, Truck, etc.

#### 4. car_brands (Car Brands)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| brand_id | BIGINT | PK | Random 6-digit ID |
| brand_name | VARCHAR(100) | UK, NOT NULL | Unique brand name |

**Features:**
- Auto-generated random ID (100000 + random 900000)
- Examples: Toyota, Honda, BMW, Mercedes, etc.

#### 5. cars (Car Inventory) ⚠️ UPDATED
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| car_id | BIGINT | PK | Random 6-digit ID |
| brand | ENUM | NOT NULL | Brand enum (TOYOTA, HYUNDAI, MERCEDES, VINFAST) |
| category | ENUM | NOT NULL | Category enum (SUV, SEDAN, HATCHBACK) |
| model | VARCHAR(50) | NOT NULL | Car model name |
| manufacture_year | INT | NOT NULL | Year of manufacture |
| price | DECIMAL(15,3) | NOT NULL | Car price |
| color | ENUM | | Car color (enum) |
| description | TEXT | | Car description |
| status | ENUM | | AVAILABLE/SOLD |
| image_url | VARCHAR(255) | | Car image URL |

**⚠️ IMPORTANT CHANGE:**
- **Previously:** Used foreign keys to CarBrand and CarCategory entities (brand_id, category_id)
- **Now:** Uses enum fields (brand, category) - Brand and Category are no longer entity relationships
- **Migration Required:** Old brand_id and category_id columns must be manually dropped from database

**Enums:**
- **Brand:** TOYOTA, HYUNDAI, MERCEDES, VINFAST
- **Category:** SUV, SEDAN, HATCHBACK
- **CarStatus:** AVAILABLE, SOLD

**Relationships:**
- One-to-many with OrderDetail (LAZY fetch)

#### 6. car_details (Detailed Car Specifications)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| car_detail_id | BIGINT | PK | Random 9-digit ID |
| car_id | BIGINT | FK, UK, NOT NULL | One-to-one with cars |
| engine | VARCHAR(100) | NOT NULL | Engine specifications |
| horsepower | INT | NOT NULL | Horsepower (HP) |
| torque | INT | NOT NULL | Torque (Nm) |
| transmission | VARCHAR(50) | NOT NULL | Transmission type |
| fuel_type | VARCHAR(30) | NOT NULL | Fuel type |
| fuel_consumption | VARCHAR(50) | NOT NULL | Fuel consumption rate |
| seats | INT | NOT NULL | Number of seats |
| weight | DOUBLE | NOT NULL | Vehicle weight (kg) |
| dimensions | VARCHAR(100) | NOT NULL | Dimensions (LxWxH mm) |

**Relationships:**
- One-to-one with Car (LAZY fetch)
- Foreign key: fk_car_details_car

#### 7. orders (Order Management) 🆕 NEW
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_id | VARCHAR(36) | PK | Auto-generated UUID |
| user_id | VARCHAR(36) | FK, NOT NULL | Reference to users |
| full_name | VARCHAR(100) | NOT NULL | Customer full name (snapshot) |
| email | VARCHAR(100) | NOT NULL | Customer email (snapshot) |
| phone | VARCHAR(20) | NOT NULL | Customer phone (snapshot) |
| address | VARCHAR(255) | NOT NULL | Shipping address |
| city | VARCHAR(50) | | City |
| district | VARCHAR(50) | | District |
| ward | VARCHAR(50) | | Ward |
| note | TEXT | | Order notes |
| subtotal | DECIMAL(15,2) | NOT NULL | Sum of order items |
| shipping_fee | DECIMAL(10,2) | NOT NULL | Shipping cost (default 0) |
| tax | DECIMAL(10,2) | NOT NULL | Tax amount (default 0) |
| total_amount | DECIMAL(15,2) | NOT NULL | Final total amount |
| order_date | DATETIME | NOT NULL | Order date/time |
| status | ENUM | NOT NULL | Order status |
| cancel_reason | VARCHAR(255) | | Reason for cancellation |
| created_at | DATETIME | NOT NULL | Auto-set on creation |
| updated_at | DATETIME | NOT NULL | Auto-updated on modification |

**Enums:**
- **OrderStatus:** PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED, COMPLETED

**Relationships:**
- Many-to-one with User (LAZY fetch)
- One-to-many with OrderDetail (CASCADE ALL, orphanRemoval)
- One-to-one with Payment (CASCADE ALL)

**Features:**
- UUID-based primary key
- @PrePersist sets default values (UUID, orderDate, status=PENDING, fees=0)
- Shipping information is a snapshot (not a reference to user profile)
- Financial breakdown: subtotal + shipping_fee + tax = total_amount

#### 8. order_details (Order Line Items) 🆕 NEW
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_detail_id | BIGINT | PK | Auto-increment ID |
| order_id | VARCHAR(36) | FK, NOT NULL | Reference to orders |
| car_id | BIGINT | FK, NOT NULL | Reference to cars |
| color_name | ENUM | | Color of the car (snapshot) |
| quantity | INT | NOT NULL | Item quantity (default 1) |
| price | DECIMAL(15,2) | NOT NULL | Car price at order time (snapshot) |

**Relationships:**
- Many-to-one with Order (LAZY fetch)
- Many-to-one with Car (LAZY fetch)

**Features:**
- Price is a snapshot at order time (not a live reference to car.price)
- @PrePersist sets default quantity to 1
- CASCADE delete when order is deleted
- Foreign key: fk_order_details_order, fk_order_details_car

#### 9. payment (Payment Records) 🆕 NEW
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| payment_id | VARCHAR(36) | PK | Auto-generated UUID |
| order_id | VARCHAR(36) | FK, UK, NOT NULL | One-to-one with orders |
| payment_date | DATETIME | | Date of successful payment |
| amount | DECIMAL(15,2) | NOT NULL | Payment amount |
| payment_method | ENUM | NOT NULL | Payment method |
| status | ENUM | NOT NULL | Payment status |
| transaction_id | VARCHAR(255) | | External payment gateway transaction ID |
| created_at | DATETIME | NOT NULL | Auto-set on creation |
| updated_at | DATETIME | NOT NULL | Auto-updated on modification |

**Enums:**
- **PaymentMethod:** CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, VNPAY, MOMO
- **PaymentStatus:** PENDING, SUCCESS, FAILED

**Relationships:**
- One-to-one with Order (LAZY fetch, unique constraint)

**Features:**
- UUID-based primary key
- @PrePersist sets default status to PENDING
- payment_date is set when status becomes SUCCESS
- Supports Vietnamese payment gateways (VNPAY, MOMO)
- Foreign key: fk_payment_order

---

## 🔒 Security Configuration

### Authentication & Authorization

The application uses **JWT-based authentication** with **role-based access control (RBAC)**.

#### Security Components (SecurityConfig.java)

**Key Features:**
- **Session Management:** STATELESS (no server-side sessions)
- **CSRF:** Disabled (suitable for REST APIs)
- **CORS:** Enabled with custom configuration
- **Authentication:** JWT Bearer Token
- **Password Encoding:** BCrypt (via PasswordEncoder bean)

#### JWT Configuration

| Property | Value | Description |
|----------|-------|-------------|
| jwt.secret | ${JWT_SECRET} | Environment variable (64-byte base64 encoded) |
| jwt.expiration | 3600000 ms | Access token expires in 1 hour |
| jwt.refreshExpiration | 8640000 ms | Refresh token expires in 24 hours |

**JWT Generation Instructions (README):**
```bash
# Generate JWT secret on Windows with OpenSSL
openssl rand -base64 64
# Set as environment variable: JWT_SECRET
```

#### Role-Based Access Control

**Roles:**
- **USER** - Regular users (customers)
- **ADMIN** - Administrators (car shop managers)

**Permission Matrix:**

| Endpoint Pattern | Method | USER | ADMIN | PUBLIC |
|-----------------|--------|------|-------|--------|
| /api/auth/** | POST | ✓ | ✓ | ✓ |
| /api/cars/** | GET | ✓ | ✓ | ✓ |
| /api/car-categories/** | GET | ✓ | ✓ | ✓ |
| /api/car-brands/** | GET | ✓ | ✓ | ✓ |
| /api/car-details/** | GET | ✓ | ✓ | ✓ |
| /api/cars/** | POST | ✗ | ✓ | ✗ |
| /api/cars/** | PUT | ✗ | ✓ | ✗ |
| /api/cars/** | DELETE | ✗ | ✓ | ✗ |
| /api/car-categories/** | POST/PUT/DELETE | ✗ | ✓ | ✗ |
| /api/car-brands/** | POST/PUT/DELETE | ✗ | ✓ | ✗ |
| /api/car-details/** | POST/PUT/DELETE | ✗ | ✓ | ✗ |
| /api/accounts/** | POST/PUT/DELETE | ✗ | ✓ | ✗ |
| /api/users/** | POST/PUT/DELETE | ✓ | ✓ | ✗ |
| /swagger-ui/** | GET | ✓ | ✓ | ✓ |
| /v3/api-docs/** | GET | ✓ | ✓ | ✓ |

**Security Filters:**
1. **JwtAuthenticationFilter** - Validates JWT tokens on each request
2. **UsernamePasswordAuthenticationFilter** - Standard Spring Security filter

**Exception Handlers:**
- **AuthEntryPointJwt** - Handles authentication errors (401 Unauthorized)
- **AccessDeniedHandlerImpl** - Handles authorization errors (403 Forbidden)

**User Details Service:**
- **CustomUserDetailsService** - Loads user from database for authentication

---

## 🌐 API Endpoints

### Base URL: `http://localhost:8080/carshop`

### Authentication APIs (AuthController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/auth/register | Register new account | No |
| POST | /api/auth/login | Login and get JWT tokens | No |
| POST | /api/auth/refresh | Refresh access token | No |

**Request/Response Models:**
- **SignupRequest:** { username, password }
- **LoginRequest:** { username, password }
- **JwtResponse:** { token, refreshToken }

---

### Account Management (AccountController) ⚠️ DEPRECATED

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/accounts | ⚠️ DEPRECATED - Get all accounts | Admin |
| POST | /api/accounts | ⚠️ DEPRECATED - Create new account | Admin |
| GET | /api/accounts/{accountId} | ⚠️ DEPRECATED - Get account detail | Admin |
| PUT | /api/accounts/{accountId} | ⚠️ DEPRECATED - Update account | Admin |
| DELETE | /api/accounts/{accountId} | ⚠️ DEPRECATED - Delete account | Admin |

**⚠️ DEPRECATION NOTICE:**
AccountController is deprecated. Use UserController instead. Account information is now included in User endpoints due to the 1:1 relationship between User and Account.

**Request/Response Models:**
- **AccountRequest:** { username, email, password, role, status }
- **AccountResponse:** { username, email, role, status }

---

### User Management (UserController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/users | Get all users with account info | User/Admin |
| POST | /api/users/create-with-account | **NEW:** Create user with account (single request) | Public |
| POST | /api/users/{accountId} | Create user profile (legacy) | User/Admin |
| GET | /api/users/{userId} | Get user with account details | User/Admin |
| GET | /api/users/username/{username} | **NEW:** Get user by username | User/Admin |
| PUT | /api/users/{userId} | Update user and account info | User/Admin |
| DELETE | /api/users/{userId} | Delete user | User/Admin |
| GET | /api/users/avatar/image/{filename} | Get avatar image | Public |

**Request/Response Models:**
- **UserAccountRequest:** { username, email, password, role, status, fullName, dob, gender, phone, address, avatarFile } - **NEW:** For creating complete user profile
- **UserUpdateRequest:** { fullName, dob, gender, phone, address, email, password, role, status } - **NEW:** For updating both user and account
- **UserRequest:** { fullName, dob, gender, phone, address, avatarFile } - Legacy format
- **UserResponse:** { userId, fullName, dob, gender, phone, address, avatarUrl, **accountId, username, email, role, status** } - **UPDATED:** Now includes account fields
- **Gender Enum:** MALE, FEMALE, OTHER

**Features:**
- ✅ **Consolidated API:** Account information included in all User responses (1:1 relationship)
- ✅ **Single-request registration:** Create both Account and User in one API call
- ✅ **Comprehensive updates:** Update both user and account fields in one request
- ✅ **Username lookup:** Find user by account username
- ✅ Multipart form data support for avatar upload
- ✅ Image serving endpoint for avatars
- ✅ Password BCrypt hashing on creation/update
- ✅ Atomic transactions for data consistency

---

### Car Management (CarController) ⚠️ UPDATED

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/cars | Get all cars | Public |
| POST | /api/cars | Create new car with Brand/Category enums | Admin |
| GET | /api/cars/{carId} | Get car detail | Public |
| PUT | /api/cars/{carId} | Update car with Brand/Category enums | Admin |
| DELETE | /api/cars/{carId} | Delete car | Admin |
| GET | /api/cars/category/{category} | Get cars by Category enum (SUV, SEDAN, HATCHBACK) | Public |
| GET | /api/cars/brand/{brand} | Get cars by Brand enum (TOYOTA, HYUNDAI, MERCEDES, VINFAST) | Public |
| GET | /api/cars/image/{filename} | Get car image | Public |

**Request/Response Models:**
- **CarRequest:** { brand (ENUM), category (ENUM), model, manufactureYear, price, color, description, status, imageFile }
- **CarResponse:** { carId, brand (ENUM), category (ENUM), model, manufactureYear, price, color, description, status, imageUrl }
- **Brand Enum:** TOYOTA, HYUNDAI, MERCEDES, VINFAST
- **Category Enum:** SUV, SEDAN, HATCHBACK
- **CarStatus Enum:** AVAILABLE, SOLD

**Features:**
- ⚠️ **BREAKING CHANGE:** Now uses enum path variables instead of IDs
- Multipart form data support for car image upload
- Filter by category enum or brand enum
- Image serving endpoint
- @Valid annotation for validation

---

### Car Brand Management (CarBrandController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/car-brands | Get all car brands | Public |
| POST | /api/car-brands | Create new brand | Admin |
| GET | /api/car-brands/{brandId} | Get brand detail | Public |
| PUT | /api/car-brands/{brandId} | Update brand | Admin |
| DELETE | /api/car-brands/{brandId} | Delete brand | Admin |

**Request/Response Models:**
- **CarBrandRequest:** { brandName }
- **CarBrandResponse:** { brandId, brandName }

---

### Car Category Management (CarCategoryController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/car-categories | Get all categories | Public |
| POST | /api/car-categories | Create new category | Admin |
| GET | /api/car-categories/{categoryId} | Get category detail | Public |
| PUT | /api/car-categories/{categoryId} | Update category | Admin |
| DELETE | /api/car-categories/{categoryId} | Delete category | Admin |

**Request/Response Models:**
- **CarCategoryRequest:** { categoryName }
- **CarCategoryResponse:** { categoryId, categoryName }

---

### Car Details Management (CarDetailController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/car-details | Get all car details | Public |
| POST | /api/car-details/{carId} | Create car detail | Admin |
| GET | /api/car-details/{carDetailId} | Get car detail | Public |
| PUT | /api/car-details/{carDetailId} | Update car detail | Admin |
| DELETE | /api/car-details/{carDetailId} | Delete car detail | Admin |

**Request/Response Models:**
- **CarDetailRequest:** { engine, horsepower, torque, transmission, fuelType, fuelConsumption, seats, weight, dimensions }
- **CarDetailResponse:** { carDetailId, carId, engine, horsepower, torque, transmission, fuelType, fuelConsumption, seats, weight, dimensions }

---

### Order Management (OrderController) 🆕 NEW

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/orders | Create new order with order details and payment | User/Admin |
| GET | /api/orders | Get all orders | User/Admin |
| GET | /api/orders/{orderId} | Get order detail by order ID | User/Admin |
| GET | /api/orders/user/{userId} | Get all orders by user ID | User/Admin |
| GET | /api/orders/status/{status} | Get orders by status (PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED, COMPLETED) | User/Admin |
| PATCH | /api/orders/{orderId}/status | Update order status | User/Admin |
| DELETE | /api/orders/{orderId} | Delete order | User/Admin |

**Request/Response Models:**
- **OrderRequest:** { userId, fullName, email, phone, address, city, district, ward, note, shippingFee, tax, paymentMethod, orderDetails[] }
- **OrderResponse:** { orderId, userId, fullName, email, phone, address, city, district, ward, note, subtotal, shippingFee, tax, totalAmount, orderDate, status, createdAt, updatedAt, orderDetails[], payment }
- **OrderStatus Enum:** PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED, COMPLETED

**Features:**
- **Atomic transaction:** Creates order, order details, and payment in a single request
- **Auto-calculation:** Subtotal and total amount calculated from order details
- **Price snapshot:** Car prices are captured at order time
- **Validation:** Validates user existence and car availability
- **@Valid annotation:** Request body validation
- **Status tracking:** Update order status independently

---

### Order Detail Management (OrderDetailController) 🆕 NEW

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/order-details | Get all order details | User/Admin |
| GET | /api/order-details/{orderDetailId} | Get order detail by ID | User/Admin |
| GET | /api/order-details/order/{orderId} | Get all order details by order ID | User/Admin |
| DELETE | /api/order-details/{orderDetailId} | Delete order detail | User/Admin |

**Request/Response Models:**
- **OrderDetailRequest:** { carId, quantity }
- **OrderDetailResponse:** { orderDetailId, carId, carModel, quantity, price, subtotal }
- **Subtotal calculation:** price × quantity

**Features:**
- **Car information:** Includes car model in response
- **Subtotal calculation:** Auto-calculated in response
- **Validation:** Ensures quantity is at least 1
- **Read operations:** Primarily for viewing order details

---

### Payment Management (PaymentController) 🆕 NEW

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/payments | Create new payment for an order | User/Admin |
| GET | /api/payments | Get all payments | User/Admin |
| GET | /api/payments/{paymentId} | Get payment detail by payment ID | User/Admin |
| GET | /api/payments/order/{orderId} | Get payment by order ID | User/Admin |
| GET | /api/payments/status/{status} | Get payments by status (PENDING, SUCCESS, FAILED) | User/Admin |
| PATCH | /api/payments/{paymentId}/status | Update payment status | User/Admin |
| DELETE | /api/payments/{paymentId} | Delete payment | User/Admin |

**Request/Response Models:**
- **PaymentRequest:** { orderId, paymentDate, amount, paymentMethod, status, transactionId }
- **PaymentResponse:** { paymentId, orderId, paymentDate, amount, paymentMethod, status, transactionId, createdAt, updatedAt }
- **PaymentMethod Enum:** CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, VNPAY, MOMO
- **PaymentStatus Enum:** PENDING, SUCCESS, FAILED

**Features:**
- **Vietnamese payment gateways:** Supports VNPAY and MOMO
- **Transaction tracking:** External transaction ID support
- **Auto payment date:** Sets payment_date when status becomes SUCCESS
- **Status management:** Update payment status independently
- **Validation:** Ensures order exists before creating payment

---

### API Response Format

All API responses follow a consistent format using **ApiResponse<T>** wrapper:

```json
{
  "code": 200,
  "message": "Success",
  "data": { /* Response data */ }
}
```

**Error Response (ErrorResponse):**
```json
{
  "code": 400,
  "message": "Error message",
  "data": null
}
```

---

## ✨ Key Features

### 1. Authentication & Authorization
- ✅ JWT-based authentication (Bearer Token)
- ✅ User registration and login
- ✅ Token refresh mechanism
- ✅ Role-based access control (USER/ADMIN)
- ✅ Account status management (ACTIVE/INACTIVE/BANNED)
- ✅ BCrypt password encryption

### 2. User & Account Management (Consolidated)
- ✅ **NEW: Consolidated User + Account API** - Single endpoint returns complete user and account info
- ✅ **NEW: Single-request user creation** - Create both account and user profile in one atomic transaction
- ✅ **NEW: Combined updates** - Update user and account fields (including password) in one request
- ✅ **NEW: Username-based lookup** - Find user by account username
- ✅ User profile creation and management
- ✅ Avatar upload support (max 10MB)
- ✅ Gender selection (MALE/FEMALE/OTHER)
- ✅ Phone number uniqueness validation
- ✅ One-to-one relationship with account (eager loading)
- ✅ Password BCrypt encoding on creation/update
- ⚠️ **AccountController deprecated** - Use UserController instead

### 3. Car Inventory Management (⚠️ UPDATED - Enum Migration)
- ✅ Complete CRUD operations for cars
- ✅ Car image upload and serving
- ✅ Car status tracking (AVAILABLE/SOLD)
- ✅ **NEW: Brand enum** (TOYOTA, HYUNDAI, MERCEDES, VINFAST) - replaces CarBrand entity
- ✅ **NEW: Category enum** (SUV, SEDAN, HATCHBACK) - replaces CarCategory entity
- ✅ Filter cars by brand enum
- ✅ Filter cars by category enum
- ✅ Auto-generated car IDs
- ⚠️ **BREAKING CHANGE:** Brand/Category now stored as enums, not entity relationships

### 4. Car Brand Management
- ✅ Manage car brands (Toyota, Honda, etc.)
- ✅ Unique brand name constraint
- ✅ Admin-only access for modifications

### 5. Car Category Management
- ✅ Manage car categories (SUV, Sedan, etc.)
- ✅ Unique category name constraint
- ✅ Admin-only access for modifications

### 6. Car Details/Specifications
- ✅ Detailed technical specifications
- ✅ Engine details (type, horsepower, torque)
- ✅ Transmission type
- ✅ Fuel type and consumption
- ✅ Physical dimensions and weight
- ✅ Seating capacity
- ✅ One-to-one relationship with car

### 7. Order Management System 🆕 NEW
- ✅ **Complete order lifecycle management**
- ✅ **Order creation** with multiple items in single transaction
- ✅ **Auto-calculation** of subtotal, tax, shipping, and total amount
- ✅ **Order status tracking** (PENDING → CONFIRMED → SHIPPING → DELIVERED → COMPLETED)
- ✅ **Order cancellation** support (CANCELLED status)
- ✅ **User order history** - View all orders by user
- ✅ **Filter by status** - Track orders by their current status
- ✅ **Shipping information snapshot** - Captures customer details at order time
- ✅ **UUID-based order IDs** for security and uniqueness

### 8. Order Details Management 🆕 NEW
- ✅ **Line item management** for orders
- ✅ **Price snapshot** - Captures car price at order time
- ✅ **Quantity management** with validation
- ✅ **Subtotal calculation** per line item
- ✅ **Car information** included in response
- ✅ **Cascade delete** with parent order

### 9. Payment Management 🆕 NEW
- ✅ **Multiple payment methods** (Cash, Cards, Bank Transfer, VNPAY, MOMO)
- ✅ **Vietnamese payment gateway support** (VNPAY, MOMO)
- ✅ **Payment status tracking** (PENDING, SUCCESS, FAILED)
- ✅ **Transaction ID tracking** for external payment gateways
- ✅ **Auto payment date** - Sets when payment succeeds
- ✅ **One-to-one relationship** with orders
- ✅ **Payment history** - View all payments by status

### 10. File Upload
- ✅ Image upload for cars (multipart/form-data)
- ✅ Avatar upload for users (multipart/form-data)
- ✅ File size limit: 10MB
- ✅ Image serving endpoints

### 11. API Documentation
- ✅ Swagger UI integration
- ✅ OpenAPI 3.1.0 specification
- ✅ Interactive API testing
- ✅ Accessible at: /swagger-ui/index.html

### 12. Error Handling
- ✅ Global exception handler
- ✅ Custom exception types (AppException, BadRequestException, DuplicateKeyException)
- ✅ Standardized error responses
- ✅ ErrorCode enumeration
- ✅ **NEW error codes:** ORDER_NOT_FOUND, ORDER_DETAIL_NOT_FOUND, PAYMENT_NOT_FOUND, ORDER_CANNOT_BE_CANCELLED, NEWS_NOT_FOUND, PROMOTION_NOT_FOUND

### 13. Data Validation
- ✅ Bean validation with javax.validation
- ✅ Unique constraint enforcement
- ✅ Not null validation
- ✅ Custom business logic validation

### 14. CORS Support
- ✅ Custom CORS configuration
- ✅ Configurable allowed origins
- ✅ Debug logging enabled

### 15. Database Features
- ✅ Auto table creation/update (ddl-auto=update)
- ✅ SQL logging enabled
- ✅ Formatted SQL output
- ✅ JPA lifecycle callbacks (@PrePersist, @PreUpdate)
- ✅ Lazy loading for relationships
- ✅ **NEW:** Enum support with @Enumerated(EnumType.STRING)

---

## ⚙️ Configuration

### Application Properties (application.properties)

#### Server Configuration
```properties
server.port=8080
server.servlet.context-path=/carshop
spring.application.name=oto-shop
```

#### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/car_sales_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

#### JPA/Hibernate Configuration
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

#### File Upload Configuration
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

#### JWT Configuration
```properties
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000        # 1 hour (3600000ms)
jwt.refreshExpiration=8640000  # 24 hours (86400000ms)
```

#### Swagger Configuration
```properties
swagger.title=Auto88 - car Shop API
swagger.version=1.0.0
swagger.description=API for Auto88 car sales system
swagger.contact-name=Auto88 Dev Team
swagger.contact-email=congphamdz2004@gmail.com
```

#### Logging Configuration
```properties
logging.level.org.springframework.web.cors=DEBUG
logging.level.org.springframework.security.web.access=DEBUG
```

#### Active Profile
```properties
spring.profiles.active=dev
```

---

## 📁 Project Structure

### Directory Layout

```
oto-shop/
├── .git/                      # Git repository
├── .idea/                     # IntelliJ IDEA configuration
├── .mvn/                      # Maven wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/carshop/oto_shop/
│   │   │       ├── common/
│   │   │       │   ├── config/         # 7 configuration classes
│   │   │       │   ├── exceptions/     # 6 exception classes
│   │   │       │   └── response/       # API response wrapper
│   │   │       ├── controllers/        # 7 REST controllers
│   │   │       ├── dto/               # DTOs organized by feature
│   │   │       ├── entities/          # 6 JPA entities
│   │   │       ├── enums/             # Enumeration types
│   │   │       ├── mappers/           # MapStruct mappers
│   │   │       ├── repositories/      # 6 Spring Data repositories
│   │   │       ├── security/          # Security components
│   │   │       │   ├── handers/
│   │   │       │   ├── jwt/
│   │   │       │   ├── models/
│   │   │       │   └── services/
│   │   │       └── services/          # 7 business logic services
│   │   └── resources/
│   │       └── application.properties
│   └── test/                  # Test classes
├── target/                    # Compiled classes
├── uploads/                   # Uploaded files (images)
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw                       # Maven wrapper (Unix)
├── mvnw.cmd                   # Maven wrapper (Windows)
├── pom.xml                    # Maven configuration
└── README.md                  # Project documentation
```

### Key Files Count

- **Controllers:** 10 (Account⚠️, Auth, Car⚠️, CarBrand, CarCategory, CarDetail, User, Order🆕, OrderDetail🆕, Payment🆕)
- **Services:** 10 (matching controllers + OrderService, OrderDetailService, PaymentService)
- **Repositories:** 9 (Account, Car, CarBrand, CarCategory, CarDetail, User, Order🆕, OrderDetail🆕, Payment🆕)
- **Entities:** 9 (Account, Car⚠️, CarBrand, CarCategory, CarDetail, User, Order🆕, OrderDetail🆕, Payment🆕)
- **Enums:** 9 (AccountStatus, Brand🆕, CarStatus, Category🆕, Gender, OrderStatus🆕, PaymentMethod🆕, PaymentStatus🆕, Role)
- **Configuration Classes:** 7 (ApplicationInit, CORS, JWT, Password, Security, Swagger, Web)
- **Security Components:** Custom UserDetailsService, JWT Filter, Exception Handlers

---

## 🚀 Running the Application

### Prerequisites
1. **Java Development Kit (JDK) 21** - Installed and added to PATH
2. **Apache Maven 3.9.11** - Installed and added to PATH
3. **MySQL Server** - Running on localhost:3306
4. **MySQL Database** - Create database: `car_sales_db`
5. **JWT Secret** - Set environment variable `JWT_SECRET`

### Generate JWT Secret (Windows)
```bash
# Using Git Bash or WSL
openssl rand -base64 64

# Copy the output and set as environment variable
# Windows: System Properties > Environment Variables
# Name: JWT_SECRET
# Value: <paste the generated string>
```

### Run Application
```bash
# Navigate to project directory
cd D:\MyProject\oto-shop

# Run with Maven
mvn spring-boot:run

# Or from IDE terminal
# Application will start on http://localhost:8080/carshop
```

### Access Points
- **API Base URL:** http://localhost:8080/carshop
- **Swagger UI:** http://localhost:8080/carshop/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/carshop/v3/api-docs

---

## 📊 API Endpoints Summary

### Total Endpoints: 70+

| Controller | GET | POST | PUT/PATCH | DELETE | Total | Status |
|------------|-----|------|-----------|--------|-------|--------|
| AuthController | 0 | 3 | 0 | 0 | 3 | Active |
| AccountController | 2 | 1 | 1 | 1 | 5 | ⚠️ Deprecated |
| UserController | 4 | 2 | 1 | 1 | 8 | Active ⭐ Updated |
| CarController | 5 | 1 | 1 | 1 | 8 | Active ⚠️ Updated (Enums) |
| CarBrandController | 2 | 1 | 1 | 1 | 5 | Active |
| CarCategoryController | 2 | 1 | 1 | 1 | 5 | Active |
| CarDetailController | 2 | 1 | 1 | 1 | 5 | Active |
| OrderController 🆕 | 5 | 2 | 1 | 1 | 9 | Active ⭐ Updated |
| OrderDetailController 🆕 | 3 | 0 | 0 | 1 | 4 | Active NEW |
| PaymentController 🆕 | 5 | 3 | 1 | 1 | 10 | Active ⭐ Updated |
| NewsController 🆕 | 2 | 1 | 1 | 1 | 5 | Active NEW |
| PromotionController 🆕 | 1 | 1 | 1 | 1 | 4 | Active NEW |
| SearchController 🆕 | 1 | 0 | 0 | 0 | 1 | Active NEW |
| CompareController 🆕 | 1 | 0 | 0 | 0 | 1 | Active NEW |
| AdminDashboardController 🆕 | 3 | 0 | 0 | 0 | 3 | Active NEW |
| HomeController 🆕 | 1 | 0 | 0 | 0 | 1 | Active NEW |

**Recent Changes:**
- 🆕 **OrderController added:** Complete order management (9 endpoints), including order cancellation.
- 🆕 **OrderDetailController added:** Order line items management (4 endpoints)
- 🆕 **PaymentController added:** Payment processing (10 endpoints), including payment confirmation and failure marking.
- ⚠️ **CarController updated:** Now uses Brand/Category enums instead of IDs (BREAKING CHANGE)
- ⭐ UserController expanded with 2 new endpoints (create-with-account, username lookup)
- ⚠️ AccountController marked as deprecated (functionality moved to UserController)
- 🆕 **NewsController added:** News management (5 endpoints)
- 🆕 **PromotionController added:** Promotion management (4 endpoints)
- 🆕 **SearchController added:** Car search endpoint
- 🆕 **CompareController added:** Car comparison endpoint
- 🆕 **AdminDashboardController added:** Admin dashboard statistics endpoints
- 🆕 **HomeController added:** Home page sections endpoint

---

## 🔧 Development Notes

### Application Initialization
- **ApplicationInitConfig** - Initializes application on startup
- Default admin account creation (if configured)

### Password Security
- **PasswordConfig** - BCrypt password encoder bean
- Strong password hashing

### CORS Configuration
- **CorsConfig** - Configurable cross-origin resource sharing
- Debug logging enabled

### JWT Security
- **JwtSecretChecker** - Validates JWT secret on startup
- Ensures JWT_SECRET environment variable is set

### Swagger Configuration
- **SwaggerConfig** - OpenAPI 3 documentation setup
- Bearer Authentication scheme configured
- Contact information included

### Web Configuration
- **WebConfig** - Additional web MVC configuration
- Custom converters and interceptors (if any)

---

## 📝 Summary

This is a **well-structured Spring Boot application** following best practices:

✅ **Clean Architecture** - Layered design with clear separation of concerns
✅ **Security-First** - JWT authentication with role-based authorization
✅ **RESTful Design** - Standard HTTP methods and resource naming
✅ **Documentation** - Swagger/OpenAPI integration for API docs
✅ **Validation** - Bean validation and custom business rules
✅ **Error Handling** - Global exception handling with custom exceptions
✅ **File Management** - Image upload and serving capabilities
✅ **Database Design** - Normalized schema with proper relationships
✅ **Configuration Management** - Externalized configuration with profiles
✅ **Code Quality** - Use of Lombok and MapStruct for cleaner code

### Recent Updates (October 9, 2025)

#### ⭐ User & Account API Consolidation
- **Consolidated User + Account information** in single API responses (1:1 relationship)
- **New endpoint:** `POST /api/users/create-with-account` - Creates both account and user in one request
- **New endpoint:** `GET /api/users/username/{username}` - Find user by account username
- **Updated:** `PUT /api/users/{userId}` - Now updates both user and account fields
- **Updated:** All User GET endpoints now return account information (accountId, username, email, role, status)
- **Deprecated:** AccountController - All functionality moved to UserController
- **Added DTOs:**
  - `UserAccountRequest` - Combined request for creating user with account
  - `UserUpdateRequest` - Request for updating user and account fields
- **Updated:** `UserResponse` - Now includes account fields
- **Updated:** `User` entity - Changed fetch type to EAGER for account
- **Added:** Repository method `findByAccount_Username` in UserRepository

**Benefits:**
- ✅ Reduced API calls (1 instead of 2)
- ✅ Atomic transactions ensure data consistency
- ✅ Better developer experience
- ✅ Cleaner API design respecting 1:1 relationship

**Documentation:**
- See `CONSOLIDATION_CHANGES.md` for detailed consolidation documentation
- See `NEW_API_CREATE_USER_WITH_ACCOUNT.md` for new endpoint specification

---

### Recent Updates (October 9, 2025)

#### 🆕 Order Management System Implementation
**Complete order processing system with full CRUD functionality**

**New Entities:**
- `Order.java` - Main order entity with shipping information snapshot
- `OrderDetail.java` - Order line items with price snapshots
- `Payment.java` - Payment records with Vietnamese gateway support

**New Enums:**
- `OrderStatus` - PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED, COMPLETED
- `PaymentMethod` - CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, VNPAY, MOMO
- `PaymentStatus` - PENDING, SUCCESS, FAILED

**New Controllers & APIs:**
- `OrderController` - 8 endpoints for complete order lifecycle management
- `OrderDetailController` - 4 endpoints for order line items
- `PaymentController` - 8 endpoints for payment processing

**New Services:**
- `OrderService` - Order creation with auto-calculation (subtotal, tax, shipping, total)
- `OrderDetailService` - Line item management with price snapshots
- `PaymentService` - Payment processing with status tracking

**New Repositories:**
- `OrderRepository` - findByUser_UserId, findByStatus
- `OrderDetailRepository` - findByOrder_OrderId
- `PaymentRepository` - findByOrder_OrderId, findByStatus

**New DTOs (6 total):**
- OrderRequest/Response, OrderDetailRequest/Response, PaymentRequest/Response

**New Mappers (3 total):**
- OrderMapper, OrderDetailMapper, PaymentMapper

**New Error Codes:**
- ORDER_NOT_FOUND, ORDER_DETAIL_NOT_FOUND, PAYMENT_NOT_FOUND

**Database Schema:**
- `database_order_system.sql` - Complete SQL schema with proper constraints and indexes

**Features:**
- ✅ Atomic order creation (order + details + payment in single transaction)
- ✅ Auto-calculation of subtotal, tax, shipping, and total amount
- ✅ Price snapshot at order time (immutable pricing)
- ✅ Order status lifecycle tracking
- ✅ User order history
- ✅ Vietnamese payment gateway support (VNPAY, MOMO)
- ✅ Transaction ID tracking for external gateways
- ✅ UUID-based order and payment IDs for security

---

#### ⚠️ Car Brand/Category Enum Migration (BREAKING CHANGE)
**Migrated from entity relationships to enum fields**

**Changes:**
- `Brand.java` enum - TOYOTA, HYUNDAI, MERCEDES, VINFAST (replaces CarBrand entity relationship)
- `Category.java` enum - SUV, SEDAN, HATCHBACK (replaces CarCategory entity relationship)
- `Car.java` entity - Now uses @Enumerated enum fields instead of @ManyToOne relationships
- `CarController` - Updated to accept enum path variables (e.g., `/api/cars/brand/TOYOTA`)
- `CarRepository` - Updated methods: findAllByBrand(Brand), findAllByCategory(Category)
- `CarService` - Removed CarBrandRepository and CarCategoryRepository dependencies
- `CarBrandService` & `CarCategoryService` - Removed car deletion logic (cars now independent)

**Breaking Changes:**
- ⚠️ **API Endpoints Changed:**
  - OLD: `GET /api/cars/brand/{brandId}` (numeric ID)
  - NEW: `GET /api/cars/brand/{brand}` (enum value: TOYOTA, HYUNDAI, etc.)
  - OLD: `GET /api/cars/category/{categoryId}` (numeric ID)
  - NEW: `GET /api/cars/category/{category}` (enum value: SUV, SEDAN, HATCHBACK)

- ⚠️ **DTO Changes:**
  - CarRequest: brandId/categoryId (Long) → brand/category (Enum)
  - CarResponse: brandId/categoryId (Long) → brand/category (Enum)

**Database Migration Required:**
```sql
-- ⚠️ MANUAL MIGRATION REQUIRED
-- File: fix_car_table.sql
ALTER TABLE cars DROP FOREIGN KEY IF EXISTS fk_cars_brand;
ALTER TABLE cars DROP FOREIGN KEY IF EXISTS fk_cars_category;
ALTER TABLE cars DROP COLUMN IF EXISTS brand_id;
ALTER TABLE cars DROP COLUMN IF EXISTS category_id;
```

**Benefits:**
- ✅ Simpler data model (fewer joins)
- ✅ Better performance (no entity relationships)
- ✅ Type-safe enum values
- ✅ Fixed brand/category list (no dynamic changes needed)

**⚠️ Important:**
- Old brand_id and category_id columns must be manually dropped before using the updated Car APIs
- CarBrand and CarCategory entities remain for legacy brand/category management
- Cars are now independent of brand/category entities

---

### Potential Enhancements
- Add pagination for list endpoints
- Implement search/filter functionality
- Add caching layer (Redis)
- Add unit and integration tests
- Add API rate limiting
- Implement audit logging
- Add email notification service
- Implement soft delete instead of hard delete
- Add API versioning
- Complete removal of deprecated AccountController in future version
- Add order cancellation workflow
- Add payment gateway webhook integration
- Add inventory management (stock tracking)

---

**Generated:** October 9, 2025
**Last Updated:** October 9, 2025
**Contact:** congphamdz2004@gmail.com
**Project Version:** 0.0.1-SNAPSHOT
