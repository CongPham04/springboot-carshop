package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.account.AccountRequest;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(ApiResponse.success("Thêm account thành công!"));
    }
}
