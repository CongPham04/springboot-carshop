package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.account.AccountRequest;
import com.carshop.oto_shop.dto.account.AccountResponse;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.entities.User;
import com.carshop.oto_shop.mappers.AccountMapper;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AccountService(AccountMapper accountMapper, UserRepository userRepository, AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    public void CreateAccount(AccountRequest accountRequest) {
        try{
            Account account = accountMapper.toAccount(accountRequest);
            accountRepository.save(account);
        }catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message != null) {
                if (message.contains("uk_accounts_username")) {
                    throw new DuplicateKeyException("username đã tồn tại!");
                } else if (message.contains("uk_accounts_email")) {
                    throw new DuplicateKeyException("email đã tồn tại!");
                }else if (message.contains("cannot be null")) {
                    // Lấy ra tên cột bị null từ message (ví dụ: "Column 'password' cannot be null")
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }else{
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }
    public void UpdateAccount(AccountRequest accountRequest, String accountId) {
        try{
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
            String oldPassword = account.getPassword();
            if (accountRequest.getPassword() != null && !accountRequest.getPassword().isBlank()) {
                account.setPassword(accountRequest.getPassword());
            } else {
                account.setPassword(oldPassword);
            }

            // Cập nhật các field khác từ request sang account
            accountMapper.updateAccount(accountRequest, account);

            // Lưu lại vào DB
            accountRepository.save(account);

        }catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message != null) {
                if (message.contains("uk_accounts_username")) {
                    throw new DuplicateKeyException("username đã tồn tại!");
                } else if (message.contains("uk_accounts_email")) {
                    throw new DuplicateKeyException("email đã tồn tại!");
                } else if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    @Transactional
    public void DeleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        userRepository.deleteAllByAccountId(accountId);
        accountRepository.delete(account);
    }

    public AccountResponse GetAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        return accountMapper.toAccountResponse(account);
    }

    public List<AccountResponse> GetAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toAccountResponse)
                .toList();
    }
}
