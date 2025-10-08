# Auto88 Car Shop - Spring Boot Project Analysis

**Analysis Date:** October 8, 2025
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

Auto88 Car Shop is a **RESTful API** built with **Spring Boot 3.5.5** for managing a car sales system. The application provides comprehensive functionality for car inventory management, user authentication, and role-based access control.

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
│   ├── AccountController.java
│   ├── AuthController.java
│   ├── CarBrandController.java
│   ├── CarCategoryController.java
│   ├── CarController.java
│   ├── CarDetailController.java
│   └── UserController.java
├── dto/                     # Data Transfer Objects
│   ├── account/
│   ├── auth/
│   ├── car/
│   ├── carbrand/
│   ├── carcategory/
│   ├── cardetail/
│   └── user/
│       ├── UserAccountRequest.java (NEW - combined account+user)
│       ├── UserUpdateRequest.java (NEW - update account+user)
│       ├── UserRequest.java
│       └── UserResponse.java (UPDATED - includes account fields)
├── entities/               # JPA entities
│   ├── Account.java
│   ├── Car.java
│   ├── CarBrand.java
│   ├── CarCategory.java
│   ├── CarDetail.java
│   └── User.java
├── enums/                  # Enumerations
├── mappers/                # MapStruct mappers
├── repositories/           # Spring Data JPA repositories
│   ├── AccountRepository.java
│   ├── CarBrandRepository.java
│   ├── CarCategoryRepository.java
│   ├── CarDetailRepository.java
│   ├── CarRepository.java
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
    ├── CarBrandService.java
    ├── CarCategoryService.java
    ├── CarDetailService.java
    ├── CarService.java
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
              │  users  │
              │─────────│
              │ user_id PK │
              │ account_id FK │
              │ full_name │
              │ dob       │
              │ gender    │
              │ phone UK  │
              │ address   │
              │ avatar_url│
              └─────────┘

┌─────────────────┐
│ car_categories  │
│─────────────────│
│ category_id PK  │───┐
│ category_name UK│   │
└─────────────────┘   │
                      │ N:1
┌──────────────┐      │
│  car_brands  │      │
│──────────────│      │
│ brand_id PK  │──┐   │
│ brand_name UK│  │   │
└──────────────┘  │   │
                  │ N:1
                  ↓   ↓
              ┌────────────┐
              │    cars    │
              │────────────│
              │ car_id PK  │───┐
              │ category_id FK │ │ 1:1
              │ brand_id FK │   │
              │ model       │   │
              │ manufacture_year│
              │ price       │   │
              │ color       │   │
              │ description │   │
              │ status      │   │
              │ image_url   │   │
              └────────────┘   │
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

#### 5. cars (Car Inventory)
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| car_id | BIGINT | PK | Random 6-digit ID |
| category_id | BIGINT | FK, NOT NULL | Reference to car_categories |
| brand_id | BIGINT | FK, NOT NULL | Reference to car_brands |
| model | VARCHAR(50) | NOT NULL | Car model name |
| manufacture_year | INT | NOT NULL | Year of manufacture |
| price | DECIMAL(15,3) | NOT NULL | Car price |
| color | VARCHAR(30) | | Car color |
| description | TEXT | | Car description |
| status | ENUM | | AVAILABLE/SOLD |
| image_url | VARCHAR(255) | | Car image URL |

**Relationships:**
- Many-to-one with CarCategory (LAZY fetch)
- Many-to-one with CarBrand (LAZY fetch)

**Enums:**
- **CarStatus:** AVAILABLE, SOLD

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

### Car Management (CarController)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/cars | Get all cars | Public |
| POST | /api/cars | Create new car | Admin |
| GET | /api/cars/{carId} | Get car detail | Public |
| PUT | /api/cars/{carId} | Update car | Admin |
| DELETE | /api/cars/{carId} | Delete car | Admin |
| GET | /api/cars/category/{categoryId} | Get cars by category | Public |
| GET | /api/cars/brand/{brandId} | Get cars by brand | Public |
| GET | /api/cars/image/{filename} | Get car image | Public |

**Request/Response Models:**
- **CarRequest:** { brandId, categoryId, model, manufactureYear, price, color, description, status, imageFile }
- **CarResponse:** { carId, brandId, categoryId, model, manufactureYear, price, color, description, status, imageUrl }
- **CarStatus Enum:** AVAILABLE, SOLD

**Features:**
- Multipart form data support for car image upload
- Filter by category or brand
- Image serving endpoint

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

### 3. Car Inventory Management
- ✅ Complete CRUD operations for cars
- ✅ Car image upload and serving
- ✅ Car status tracking (AVAILABLE/SOLD)
- ✅ Filter cars by brand
- ✅ Filter cars by category
- ✅ Auto-generated car IDs

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

### 7. File Upload
- ✅ Image upload for cars (multipart/form-data)
- ✅ Avatar upload for users (multipart/form-data)
- ✅ File size limit: 10MB
- ✅ Image serving endpoints

### 8. API Documentation
- ✅ Swagger UI integration
- ✅ OpenAPI 3.1.0 specification
- ✅ Interactive API testing
- ✅ Accessible at: /swagger-ui/index.html

### 9. Error Handling
- ✅ Global exception handler
- ✅ Custom exception types (AppException, BadRequestException, DuplicateKeyException)
- ✅ Standardized error responses
- ✅ ErrorCode enumeration

### 10. Data Validation
- ✅ Bean validation with javax.validation
- ✅ Unique constraint enforcement
- ✅ Not null validation
- ✅ Custom business logic validation

### 11. CORS Support
- ✅ Custom CORS configuration
- ✅ Configurable allowed origins
- ✅ Debug logging enabled

### 12. Database Features
- ✅ Auto table creation/update (ddl-auto=update)
- ✅ SQL logging enabled
- ✅ Formatted SQL output
- ✅ JPA lifecycle callbacks (@PrePersist, @PreUpdate)
- ✅ Lazy loading for relationships

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

- **Controllers:** 7 (Account, Auth, Car, CarBrand, CarCategory, CarDetail, User)
- **Services:** 7 (matching controllers)
- **Repositories:** 6 (Account, Car, CarBrand, CarCategory, CarDetail, User)
- **Entities:** 6 (Account, Car, CarBrand, CarCategory, CarDetail, User)
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

### Total Endpoints: 40+

| Controller | GET | POST | PUT | DELETE | Total | Status |
|------------|-----|------|-----|--------|-------|--------|
| AuthController | 0 | 3 | 0 | 0 | 3 | Active |
| AccountController | 2 | 1 | 1 | 1 | 5 | ⚠️ Deprecated |
| UserController | 4 | 2 | 1 | 1 | 8 | Active ⭐ Updated |
| CarController | 5 | 1 | 1 | 1 | 8 | Active |
| CarBrandController | 2 | 1 | 1 | 1 | 5 | Active |
| CarCategoryController | 2 | 1 | 1 | 1 | 5 | Active |
| CarDetailController | 2 | 1 | 1 | 1 | 5 | Active |

**Recent Changes:**
- ⭐ UserController expanded with 2 new endpoints (create-with-account, username lookup)
- ⚠️ AccountController marked as deprecated (functionality moved to UserController)

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

### Recent Updates (October 8, 2025)

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

### Potential Enhancements
- Add pagination for list endpoints
- Implement search/filter functionality
- Add caching layer (Redis)
- Implement order and payment functionality (as planned in README)
- Add unit and integration tests
- Add API rate limiting
- Implement audit logging
- Add email notification service
- Implement soft delete instead of hard delete
- Add API versioning
- Complete removal of deprecated AccountController in future version

---

**Generated:** October 8, 2025
**Last Updated:** October 8, 2025 (Added User/Account Consolidation)
**Contact:** congphamdz2004@gmail.com
**Project Version:** 0.0.1-SNAPSHOT
