package com.carshop.oto_shop.security.models;

import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // map enum Role -> ROLE_XXX
        return List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getStatus() != AccountStatus.INACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getStatus() != AccountStatus.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getStatus() == AccountStatus.ACTIVE;
    }


    public String getAccountId() {
        return account.getAccountId();
    }

    public String getRole() {
        return account.getRole().name();
    }
}
