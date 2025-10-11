package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.user.UserAccountRequest;
import com.carshop.oto_shop.dto.user.UserResponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.dto.user.UserUpdateRequest;
import com.carshop.oto_shop.services.CarService;
import com.carshop.oto_shop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Create user with account",
        description = "API to create a new user with complete account information in a single request. " +
                      "Creates both Account and User records. Supports avatar upload."
    )
    @PostMapping(value = "/create-with-account", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createUserWithAccount(
            @Valid @ModelAttribute UserAccountRequest request) {
        UserResponse response = userService.createUserWithAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo tài khoản và người dùng thành công!"));
    }

    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping(value = "/{accountId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createUser(@ModelAttribute UserRequest userRequest, @PathVariable("accountId") String accountId) {
        userService.CreateUser(userRequest, accountId);
        return ResponseEntity.ok(ApiResponse.success("Thêm người dùng thành công!"));
    }

    @Operation(
        summary = "Update user with account",
        description = "API to update user and account information. Supports updating: fullName, dob, gender, phone, address, email, password, role, status. " +
                      "Password will be BCrypt hashed automatically."
    )

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable("userId") String userId,
            @Valid @ModelAttribute UserUpdateRequest request
    ) {
        userService.updateUserWithAccount(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công!"));
    }

    @Operation(summary = "Delete user", description = "Api delete user")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("userId") String userId) {
        userService.DeleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Xoá người dùng thành công!"));
    }

    @Operation(summary = "Get user detail", description = "API get user detail")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable("userId") String userId) {
        UserResponse dataUsers = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công!",dataUsers));
    }

    @Operation(summary = "Get image", description = "API get image")
    @GetMapping("/avatar/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UserService.UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Xác định Content-Type của file
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Operation(summary = "Get all user", description = "API get all user")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> dataUsers = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công!", dataUsers));
    }

    @Operation(summary = "Get user by username", description = "API get user by username from account")
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable("username") String username) {
        UserResponse dataUser = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công!", dataUser));
    }
}
