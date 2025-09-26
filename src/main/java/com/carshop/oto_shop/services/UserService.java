package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.user.UserResponse;
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

import java.util.List;

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
            if(message != null){
                if(message.contains("uk_users_phone")) {
                    throw new DuplicateKeyException("Số điện thoại đã tồn tại!");
                }else if(message.contains(accountId)){
                    throw new DuplicateKeyException("Người dùng cho tài khoản này đã tồn tại!");
                }else if (message.contains("cannot be null")) {
                    // Lấy ra tên cột bị null từ message (ví dụ: "Column 'password' cannot be null")
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }else{
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            }else{
                throw new AppException(ErrorCode.UNKNOWN);
            }

        }

    }

    public void UpdateUser(UserRequest userRequest, String userId) {
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));
            userMapper.updateUser(userRequest, user);
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage(); //lấy message gốc từ DB
            logger.error("DataIntegrityViolationException caught: {}", message);
            if(message != null){
                if(message.contains("uk_users_phone")) {
                    throw new DuplicateKeyException("Số điện thoại đã tồn tại!");
                }else if (message.contains("cannot be null")) {
                    // Lấy ra tên cột bị null từ message (ví dụ: "Column 'password' cannot be null")
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }else{
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            }else{
                throw new AppException(ErrorCode.UNKNOWN);
            }

        }
    }

    public void DeleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
