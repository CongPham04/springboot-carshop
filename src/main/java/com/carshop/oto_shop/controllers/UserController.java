package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.user.UserReponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserRequest userRequest, @PathVariable("accountId") String accountId) {
        userService.CreateUser(userRequest, accountId);
        return ResponseEntity.ok(ApiResponse.success("Thêm người dùng thành công!"));
    }

    @Operation(summary = "Update user", description = "API update user" )
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable("userId") String userId, @RequestBody UserRequest userRequest) {
        userService.UpdateUser(userRequest, userId);
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
    public ResponseEntity<ApiResponse<UserReponse>> getUser(@PathVariable("userId") String userId) {
        UserReponse dataUsers = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công!",dataUsers));
    }

    @Operation(summary = "Get all user", description = "API get all user")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserReponse>>> getAllUsers() {
        List<UserReponse> dataUsers = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công!", dataUsers));
    }
}
