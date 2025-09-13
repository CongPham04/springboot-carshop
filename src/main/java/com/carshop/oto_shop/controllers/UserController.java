package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.user.UserReponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserRequest userRequest, @PathVariable("accountId") String accountId) {
        userService.CreateUser(userRequest, accountId);
        return ResponseEntity.ok(ApiResponse.success("Thêm người dùng thành công!"));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable("userId") String userId, @RequestBody UserRequest userRequest) {
        userService.UpdateUser(userRequest, userId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công!"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("userId") String userId) {
        userService.DeleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Xoá người dùng thành công!"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserReponse>> getUser(@PathVariable("userId") String userId) {
        UserReponse dataUser = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công!",dataUser));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserReponse>>> getAllUsers() {
        List<UserReponse> dataUsers = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin của các người dùng thành công!", dataUsers));
    }
}
