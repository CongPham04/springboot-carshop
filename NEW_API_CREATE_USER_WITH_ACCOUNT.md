# New API: Create User with Account

**Date:** October 8, 2025
**Endpoint:** `POST /api/users/create-with-account`
**Purpose:** Create both Account and User records in a single atomic transaction

---

## 🎯 Overview

This new API endpoint allows creating a complete user profile including account credentials and personal information in **one request**, eliminating the need for two separate API calls.

### Benefits

✅ **Single transaction** - Both records created atomically (rollback on failure)
✅ **Better UX** - One-step user registration
✅ **Data consistency** - No orphaned accounts or users
✅ **Comprehensive validation** - All constraints enforced at request time
✅ **Avatar upload support** - Optional profile picture in same request

---

## 📋 API Specification

### Endpoint Details

**URL:** `POST /api/users/create-with-account`
**Content-Type:** `multipart/form-data`
**Authentication:** Not required (public registration endpoint)
**Returns:** `201 Created` with complete user + account information

---

## 📥 Request Format

### Required Fields

#### Account Information
- **username** *(string, required)*
  - Min length: 3 characters
  - Max length: 50 characters
  - Pattern: `^[a-zA-Z0-9_]+$` (alphanumeric + underscore only)
  - Unique constraint enforced
  - Example: `"johndoe123"`

- **email** *(string, required)*
  - Must be valid email format
  - Max length: 100 characters
  - Unique constraint enforced
  - Example: `"john.doe@example.com"`

- **password** *(string, required)*
  - Min length: 6 characters
  - Max length: 50 characters
  - Will be BCrypt hashed
  - Example: `"securePass123"`

#### User Information
- **fullName** *(string, required)*
  - Max length: 100 characters
  - Example: `"John Doe"`

### Optional Fields

#### Account Information
- **role** *(enum, optional)*
  - Values: `USER`, `ADMIN`
  - Default: `USER`

- **status** *(enum, optional)*
  - Values: `ACTIVE`, `INACTIVE`, `BANNED`
  - Default: `ACTIVE`

#### User Information
- **dob** *(date, optional)*
  - Format: `YYYY-MM-DD`
  - Must be past date
  - Example: `"1990-01-15"`

- **gender** *(enum, optional)*
  - Values: `MALE`, `FEMALE`, `OTHER`
  - Example: `"MALE"`

- **phone** *(string, optional)*
  - Pattern: `^[0-9]{10,20}$` (10-20 digits)
  - Unique constraint enforced
  - Example: `"0123456789"`

- **address** *(string, optional)*
  - Max length: 255 characters
  - Example: `"123 Main Street, Hanoi"`

- **avatarFile** *(file, optional)*
  - Supported formats: `image/png`, `image/jpeg`
  - Max size: 10MB
  - Example: `avatar.jpg`

---

## 🔒 Validation Rules

### Database Constraints

| Field | Constraint | Validation Message |
|-------|-----------|-------------------|
| username | Unique | "Username đã tồn tại!" |
| email | Unique | "Email đã tồn tại!" |
| phone | Unique | "Số điện thoại đã tồn tại!" |
| username | Not blank | "Username không được để trống" |
| username | Length 3-50 | "Username phải có độ dài từ 3-50 ký tự" |
| username | Pattern | "Username chỉ được chứa chữ cái, số và dấu gạch dưới" |
| email | Not blank | "Email không được để trống" |
| email | Valid email | "Email không hợp lệ" |
| email | Max 100 | "Email không được vượt quá 100 ký tự" |
| password | Not blank | "Password không được để trống" |
| password | Length 6-50 | "Password phải có độ dài từ 6-50 ký tự" |
| fullName | Not blank | "Họ tên không được để trống" |
| fullName | Max 100 | "Họ tên không được vượt quá 100 ký tự" |
| dob | Past date | "Ngày sinh phải là ngày trong quá khứ" |
| phone | Pattern | "Số điện thoại phải có từ 10-20 chữ số" |
| address | Max 255 | "Địa chỉ không được vượt quá 255 ký tự" |

### Business Rules

1. **Password Security**
   - Password is BCrypt hashed before storage
   - Never returned in responses
   - Minimum 6 characters enforced

2. **Default Values**
   - `role` defaults to `USER` if not provided
   - `status` defaults to `ACTIVE` if not provided
   - Timestamps (`created_at`, `updated_at`) auto-generated

3. **Transaction Atomicity**
   - Account created first
   - User created second (linked to account)
   - If user creation fails, account creation is rolled back
   - Database constraints enforced at transaction commit

4. **Avatar Upload**
   - Optional multipart file
   - Saved to `uploads/avatars/` directory
   - Filename: `{timestamp}_{originalname}`
   - URL returned in response

---

## 📤 Response Format

### Success Response (201 Created)

```json
{
  "code": 201,
  "message": "Tạo tài khoản và người dùng thành công!",
  "data": {
    // User information
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "dob": "1990-01-15",
    "gender": "MALE",
    "phone": "0123456789",
    "address": "123 Main Street, Hanoi",
    "avatarUrl": "http://localhost:8080/carshop/api/users/avatar/image/1696789012345_avatar.jpg",

    // Account information
    "accountId": "987e6543-e21b-12d3-a456-426614174000",
    "username": "johndoe123",
    "email": "john.doe@example.com",
    "role": "USER",
    "status": "ACTIVE"
  }
}
```

### Error Responses

#### 400 Bad Request - Validation Error
```json
{
  "code": 400,
  "message": "Username không được để trống",
  "data": null
}
```

#### 409 Conflict - Duplicate Key
```json
{
  "code": 409,
  "message": "Username đã tồn tại!",
  "data": null
}
```

#### 409 Conflict - Duplicate Email
```json
{
  "code": 409,
  "message": "Email đã tồn tại!",
  "data": null
}
```

#### 409 Conflict - Duplicate Phone
```json
{
  "code": 409,
  "message": "Số điện thoại đã tồn tại!",
  "data": null
}
```

---

## 🧪 Testing Examples

### Example 1: Basic User Creation (Minimal Fields)

```bash
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=johndoe123" \
  -F "email=john.doe@example.com" \
  -F "password=securePass123" \
  -F "fullName=John Doe"
```

**Response:**
```json
{
  "code": 201,
  "message": "Tạo tài khoản và người dùng thành công!",
  "data": {
    "userId": "uuid-here",
    "fullName": "John Doe",
    "dob": null,
    "gender": null,
    "phone": null,
    "address": null,
    "avatarUrl": null,
    "accountId": "uuid-here",
    "username": "johndoe123",
    "email": "john.doe@example.com",
    "role": "USER",
    "status": "ACTIVE"
  }
}
```

---

### Example 2: Complete User Creation (All Fields)

```bash
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=janedoe" \
  -F "email=jane@example.com" \
  -F "password=myPassword456" \
  -F "role=USER" \
  -F "status=ACTIVE" \
  -F "fullName=Jane Doe" \
  -F "dob=1995-05-20" \
  -F "gender=FEMALE" \
  -F "phone=0987654321" \
  -F "address=456 Oak Street, Ho Chi Minh City" \
  -F "avatarFile=@/path/to/avatar.jpg"
```

**Response:**
```json
{
  "code": 201,
  "message": "Tạo tài khoản và người dùng thành công!",
  "data": {
    "userId": "uuid-here",
    "fullName": "Jane Doe",
    "dob": "1995-05-20",
    "gender": "FEMALE",
    "phone": "0987654321",
    "address": "456 Oak Street, Ho Chi Minh City",
    "avatarUrl": "http://localhost:8080/carshop/api/users/avatar/image/1696789012345_avatar.jpg",
    "accountId": "uuid-here",
    "username": "janedoe",
    "email": "jane@example.com",
    "role": "USER",
    "status": "ACTIVE"
  }
}
```

---

### Example 3: Create Admin User

```bash
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=admin_user" \
  -F "email=admin@carshop.com" \
  -F "password=AdminPass123" \
  -F "role=ADMIN" \
  -F "fullName=Admin User" \
  -F "phone=0111222333"
```

---

### Example 4: Validation Error (Missing Required Field)

```bash
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=testuser" \
  -F "email=test@example.com"
  # Missing password and fullName
```

**Response:**
```json
{
  "code": 400,
  "message": "Password không được để trống",
  "data": null
}
```

---

### Example 5: Duplicate Username Error

```bash
# First request - succeeds
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=duplicateuser" \
  -F "email=user1@example.com" \
  -F "password=pass123" \
  -F "fullName=User One"

# Second request - fails with duplicate username
curl -X POST http://localhost:8080/carshop/api/users/create-with-account \
  -F "username=duplicateuser" \
  -F "email=user2@example.com" \
  -F "password=pass456" \
  -F "fullName=User Two"
```

**Response (Second Request):**
```json
{
  "code": 409,
  "message": "Username đã tồn tại!",
  "data": null
}
```

---

## 🔄 Comparison: Old vs New Approach

### Old Approach (2 Steps)

**Step 1: Create Account**
```bash
POST /api/accounts
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "pass123",
  "role": "USER"
}
# Response: accountId = "123-456-789"
```

**Step 2: Create User**
```bash
POST /api/users/123-456-789
{
  "fullName": "John Doe",
  "phone": "0123456789"
}
```

**Issues:**
❌ Two HTTP requests
❌ Not atomic (account created even if user creation fails)
❌ More complex frontend logic
❌ Potential orphaned accounts
❌ Race conditions possible

---

### New Approach (1 Step)

**Single Request:**
```bash
POST /api/users/create-with-account
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "pass123",
  "fullName": "John Doe",
  "phone": "0123456789"
}
```

**Benefits:**
✅ Single HTTP request
✅ Atomic transaction (@Transactional)
✅ Simpler frontend logic
✅ No orphaned records
✅ Better error handling

---

## 🏗️ Implementation Details

### Transaction Flow

```java
@Transactional
public UserResponse createUserWithAccount(UserAccountRequest request) {
    // 1. Create Account
    Account account = new Account();
    account.setUsername(request.getUsername());
    account.setEmail(request.getEmail());
    account.setPassword(passwordEncoder.encode(request.getPassword()));
    account.setRole(request.getRole() != null ? request.getRole() : Role.USER);
    account.setStatus(request.getStatus() != null ? request.getStatus() : AccountStatus.ACTIVE);
    Account savedAccount = accountRepository.save(account);

    // 2. Create User
    User user = new User();
    user.setAccount(savedAccount);
    user.setFullName(request.getFullName());
    user.setDob(request.getDob());
    user.setGender(request.getGender());
    user.setPhone(request.getPhone());
    user.setAddress(request.getAddress());

    // Handle avatar upload
    if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
        String avatarUrl = saveAvatar(request.getAvatarFile());
        user.setAvatarUrl(avatarUrl);
    }

    User savedUser = userRepository.save(user);

    // 3. Build and return response
    return userMapper.toUserResponse(savedUser);
}
```

### Exception Handling

The service method catches and handles:
- `DataIntegrityViolationException` → Duplicate key or null constraint violations
- Generic `Exception` → Unknown errors

All exceptions are logged with context information.

---

## 📊 Swagger Documentation

The endpoint is automatically documented in Swagger UI at:
```
http://localhost:8080/carshop/swagger-ui/index.html
```

**Swagger Metadata:**
- **Tag:** UserController
- **Summary:** Create user with account
- **Description:** API to create a new user with complete account information in a single request. Creates both Account and User records. Supports avatar upload.
- **Consumes:** multipart/form-data
- **Produces:** application/json
- **Response Codes:**
  - 201: Created successfully
  - 400: Validation error
  - 409: Duplicate key (username/email/phone)
  - 500: Internal server error

---

## 🔐 Security Considerations

### Public Access
- ⚠️ This endpoint is **NOT** protected by authentication
- Intended for **public user registration**
- To restrict access, update `SecurityConfig.java`:

```java
.requestMatchers(HttpMethod.POST, "/api/users/create-with-account")
    .hasRole(Role.ADMIN.name())
```

### Password Security
- ✅ Passwords BCrypt hashed (strength 10)
- ✅ Never logged or returned in responses
- ✅ Minimum length enforced (6 characters)
- ⚠️ Consider adding password complexity requirements

### Input Validation
- ✅ All inputs validated with `@Valid` annotation
- ✅ Bean validation rules enforced
- ✅ Database constraints as last defense

### File Upload Security
- ✅ File type validation (PNG/JPEG only)
- ✅ File size limit (10MB max)
- ✅ Unique filenames (timestamp prefix)
- ⚠️ Consider adding virus scanning
- ⚠️ Consider storing files in cloud storage (S3, etc.)

---

## 📝 Related Files

### New Files
1. `UserAccountRequest.java` - Combined DTO with validation
   - Location: `src/main/java/com/carshop/oto_shop/dto/user/`

### Modified Files
1. `UserService.java` - Added `createUserWithAccount()` method
2. `UserController.java` - Added POST endpoint
3. Both files updated with necessary imports

### Configuration Files
- `SecurityConfig.java` - May need update if restricting access
- `application.properties` - File upload limits already configured

---

## ✅ Testing Checklist

- [x] Compile project successfully
- [ ] Test with minimal required fields
- [ ] Test with all optional fields
- [ ] Test with avatar upload
- [ ] Test duplicate username validation
- [ ] Test duplicate email validation
- [ ] Test duplicate phone validation
- [ ] Test missing required fields
- [ ] Test invalid email format
- [ ] Test invalid phone format
- [ ] Test password too short
- [ ] Test username with special characters
- [ ] Test past date validation for DOB
- [ ] Test transaction rollback (account created, user fails)
- [ ] Verify avatar file is saved correctly
- [ ] Verify avatar URL is accessible
- [ ] Check Swagger documentation
- [ ] Test from frontend application

---

## 🚀 Future Enhancements

1. **Email Verification**
   - Send verification email after registration
   - Require email confirmation before activation

2. **Password Strength Meter**
   - Add complexity requirements
   - Require uppercase, lowercase, numbers, special chars

3. **CAPTCHA Integration**
   - Prevent bot registrations
   - Add reCAPTCHA or similar

4. **Rate Limiting**
   - Prevent registration abuse
   - Limit requests per IP

5. **Social Login**
   - OAuth2 integration (Google, Facebook)
   - Link social accounts to user profiles

6. **Username Availability Check**
   - Real-time validation endpoint
   - Check before form submission

7. **Profile Completion Wizard**
   - Multi-step registration form
   - Progressive data collection

---

## 📖 Documentation Updates Needed

- [ ] Update PROJECT_ANALYSIS.md with new endpoint
- [ ] Update API documentation for frontend team
- [ ] Add endpoint to Postman collection
- [ ] Update user registration guide
- [ ] Add to integration test suite

---

**Created:** October 8, 2025
**Status:** ✅ Implemented and Compiled
**Ready for Testing:** Yes
**Breaking Changes:** None (new endpoint only)
