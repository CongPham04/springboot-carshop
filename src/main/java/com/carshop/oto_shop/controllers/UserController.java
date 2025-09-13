package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.user.UserReponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
