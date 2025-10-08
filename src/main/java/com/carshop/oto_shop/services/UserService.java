package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.user.UserAccountRequest;
import com.carshop.oto_shop.dto.user.UserResponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.dto.user.UserUpdateRequest;
import com.carshop.oto_shop.entities.Account;
import com.carshop.oto_shop.entities.User;
import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.enums.Role;
import com.carshop.oto_shop.mappers.UserMapper;
import com.carshop.oto_shop.repositories.AccountRepository;
import com.carshop.oto_shop.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public static final String UPLOAD_DIR = "uploads/avatars/";
    private static final String BASE_IMAGE_URL = "http://localhost:8080/carshop/api/users/avatar/image/";

    public UserService(UserMapper userMapper, UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void CreateUser(UserRequest userRequest, String accountId) {
        try{
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
            User user = userMapper.toUser(userRequest);
            String avatarUrl = null;
            if (userRequest.getAvatarFile() != null && !userRequest.getAvatarFile().isEmpty()) {
                avatarUrl = saveAvatar(userRequest.getAvatarFile());
            }
            user.setAccount(account);
            user.setAvatarUrl(avatarUrl);
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

    private String saveAvatar(MultipartFile file) {
        try{
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/png")
                            && !contentType.equals("image/jpeg")
                            && !contentType.equals("application/pdf"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
            }

            File uploadDir = new File(UPLOAD_DIR);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return UPLOAD_DIR + fileName;
        }catch (IOException e){
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
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

    /**
     * Update both User and Account information
     * @param request Request containing user and account fields to update
     * @param userId User ID
     * @return Updated UserResponse with full information
     */
    @Transactional
    public UserResponse updateUserWithAccount(UserUpdateRequest request, String userId) {
        try {
            // Find user with account
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            Account account = user.getAccount();
            if (account == null) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
            }

            // Update User fields (only if provided)
            if (request.getFullName() != null) {
                user.setFullName(request.getFullName());
            }
            if (request.getDob() != null) {
                user.setDob(request.getDob());
            }
            if (request.getGender() != null) {
                user.setGender(request.getGender());
            }
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                user.setAddress(request.getAddress());
            }

            // Update Account fields (only if provided)
            if (request.getEmail() != null) {
                account.setEmail(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                // Encode password before saving
                account.setPassword(passwordEncoder.encode(request.getPassword()));
                logger.info("Password updated for account {}", account.getAccountId());
            }
            if (request.getRole() != null) {
                account.setRole(request.getRole());
            }
            if (request.getStatus() != null) {
                account.setStatus(request.getStatus());
            }

            // Save both (cascade or explicit)
            accountRepository.save(account);
            User savedUser = userRepository.save(user);

            logger.info("Updated user {} with account {}", userId, account.getAccountId());

            // Build response
            UserResponse response = userMapper.toUserResponse(savedUser);
            if (savedUser.getAvatarUrl() != null) {
                String fileName = Paths.get(savedUser.getAvatarUrl()).getFileName().toString();
                response.setAvatarUrl(BASE_IMAGE_URL + fileName);
            }

            return response;

        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                if (message.contains("uk_accounts_email")) {
                    throw new DuplicateKeyException("Email đã tồn tại!");
                } else if (message.contains("uk_users_phone")) {
                    throw new DuplicateKeyException("Số điện thoại đã tồn tại!");
                } else if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error updating user with account: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    private void deleteAvatarFile(String avatarUrl) {
        if (avatarUrl == null) return;
        try {
            String fileName = Paths.get(avatarUrl).getFileName().toString();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            File file = filePath.toFile();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("Đã xoá avatar: " + filePath);
                } else {
                    logger.warn("Không thể xoá avatar: " + filePath);
                }
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xoá avatar: " + e.getMessage());
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
        UserResponse response = userMapper.toUserResponse(user);
        if(user.getAvatarUrl() != null){
            // Thay "/uploads/xxx.png" thành full URL API
            String fileName = Paths.get(user.getAvatarUrl()).getFileName().toString();
            response.setAvatarUrl(BASE_IMAGE_URL + fileName);
        }
        return response;
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserResponse response = userMapper.toUserResponse(user);
                    if (user.getAvatarUrl() != null) {
                        String fileName = Paths.get(user.getAvatarUrl()).getFileName().toString();
                        response.setAvatarUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByAccount_Username(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserResponse response = userMapper.toUserResponse(user);
        if(user.getAvatarUrl() != null){
            // Thay "/uploads/xxx.png" thành full URL API
            String fileName = Paths.get(user.getAvatarUrl()).getFileName().toString();
            response.setAvatarUrl(BASE_IMAGE_URL + fileName);
        }
        return response;
    }

    /**
     * Create both Account and User in a single transaction
     * @param request Combined request containing both account and user information
     * @return UserResponse with full account and user information
     */
    @Transactional
    public UserResponse createUserWithAccount(UserAccountRequest request) {
        try {
            // Step 1: Create Account
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setEmail(request.getEmail());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setRole(request.getRole() != null ? request.getRole() : Role.USER);
            account.setStatus(request.getStatus() != null ? request.getStatus() : AccountStatus.ACTIVE);

            // Save account first
            Account savedAccount = accountRepository.save(account);
            logger.info("Created account with ID: {}", savedAccount.getAccountId());

            // Step 2: Create User
            User user = new User();
            user.setAccount(savedAccount);
            user.setFullName(request.getFullName());
            user.setDob(request.getDob());
            user.setGender(request.getGender());
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());

            // Handle avatar upload if provided
            String avatarUrl = null;
            if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
                avatarUrl = saveAvatar(request.getAvatarFile());
            }
            user.setAvatarUrl(avatarUrl);

            // Save user
            User savedUser = userRepository.save(user);
            logger.info("Created user with ID: {} for account: {}", savedUser.getUserId(), savedAccount.getAccountId());

            // Step 3: Build response
            UserResponse response = userMapper.toUserResponse(savedUser);
            if (avatarUrl != null) {
                String fileName = Paths.get(avatarUrl).getFileName().toString();
                response.setAvatarUrl(BASE_IMAGE_URL + fileName);
            }

            return response;

        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                if (message.contains("uk_accounts_username")) {
                    throw new DuplicateKeyException("Username đã tồn tại!");
                } else if (message.contains("uk_accounts_email")) {
                    throw new DuplicateKeyException("Email đã tồn tại!");
                } else if (message.contains("uk_users_phone")) {
                    throw new DuplicateKeyException("Số điện thoại đã tồn tại!");
                } else if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error creating user with account: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }
}
