package com.carshop.oto_shop.security.services;

import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Nếu repository của bạn có method findByUsernameOrEmail thì dùng, ở đây dùng fallback findAll() như bạn đã làm trước.
        Account account = accountRepository.findAll().stream()
                .filter(acc -> (acc.getUsername() != null && acc.getUsername().equals(usernameOrEmail)) ||
                        (acc.getEmail() != null && acc.getEmail().equals(usernameOrEmail)))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại: " + usernameOrEmail));
        return new CustomUserDetails(account);
    }
}
