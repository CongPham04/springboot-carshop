package com.carshop.oto_shop.dto.account;

import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.enums.Role;

public class AccountRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private AccountStatus status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
