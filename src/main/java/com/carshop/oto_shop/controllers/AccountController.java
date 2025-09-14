package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.account.AccountRequest;
import com.carshop.oto_shop.dto.account.AccountResponse;
import com.carshop.oto_shop.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.CreateAccount(accountRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm tài khoản thành công!"));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> updateAccount(@PathVariable String accountId, @RequestBody AccountRequest accountRequest) {
        accountService.UpdateAccount(accountRequest, accountId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật tài khoản thành công!"));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable String accountId) {
        accountService.DeleteAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success("Xoá tài khoản thành công!"));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountId) {
        AccountResponse dataAccount = accountService.GetAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin tài khoản thành công!", dataAccount));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> dataAccounts = accountService.GetAllAccounts();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tài khoản thành công!", dataAccounts));
    }
}
