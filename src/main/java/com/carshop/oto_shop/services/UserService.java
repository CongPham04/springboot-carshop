package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.entities.User;
import com.carshop.oto_shop.mappers.UserMapper;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository, AccountRepository accountRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public void CreateUser(UserRequest userRequest, String accountId) {
        try{
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
            User user = userMapper.toUser(userRequest);
            user.setAccount(account);

            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage(); //lấy message gốc từ DB
            logger.error("DataIntegrityViolationException caught: {}", message);
            if(message != null && message.contains("uk_users_phone")) {
                throw new DuplicateKeyException("Số điện thoại đã tồn tại!");
            }else if(message != null && message.contains(accountId)){
                throw new DuplicateKeyException("Người dùng cho tài khoản này đã tồn tại!");
            }else {
                throw new AppException(ErrorCode.DUPLICATE_KEY);
            }
        }

    }
}
