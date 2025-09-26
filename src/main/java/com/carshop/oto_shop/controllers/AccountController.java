package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.account.AccountRequest;
import com.carshop.oto_shop.dto.account.AccountResponse;
import com.carshop.oto_shop.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "AccountController")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Add account", description = "API create new account")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.CreateAccount(accountRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm tài khoản thành công!"));
    }

    @Operation(summary = "Update account", description = "API update account")
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> updateAccount(@PathVariable String accountId, @RequestBody AccountRequest accountRequest) {
        accountService.UpdateAccount(accountRequest, accountId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật tài khoản thành công!"));
    }

    @Operation(summary = "Delete account", description = "API delete account")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable String accountId) {
        accountService.DeleteAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success("Xoá tài khoản thành công!"));
    }

    @Operation(summary = "Get account detail", description = "API get account detail")
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountId) {
        AccountResponse dataAccounts = accountService.GetAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin tài khoản thành công!", dataAccounts));
    }

    @Operation(summary = "Get all account", description = "API get all account")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> dataAccounts = accountService.GetAllAccounts();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tài khoản thành công!", dataAccounts));
    }
}
