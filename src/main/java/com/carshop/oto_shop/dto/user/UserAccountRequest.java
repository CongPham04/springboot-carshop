package com.carshop.oto_shop.dto.user;

import com.carshop.oto_shop.enums.AccountStatus;
import com.carshop.oto_shop.enums.Gender;
import com.carshop.oto_shop.enums.Role;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * DTO for creating a new user with account in a single request
 * Combines Account and User information with proper validation
 */
public class UserAccountRequest {

    // ==================== Account Fields ====================

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải có độ dài từ 3-50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username chỉ được chứa chữ cái, số và dấu gạch dưới")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 50, message = "Password phải có độ dài từ 6-50 ký tự")
    private String password;

    private Role role; // Optional, defaults to USER in service

    private AccountStatus status; // Optional, defaults to ACTIVE in service

    // ==================== User Fields ====================

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String fullName;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dob;

    private Gender gender;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Số điện thoại phải có từ 10-20 chữ số")
    private String phone;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    private MultipartFile avatarFile;

    // ==================== Constructors ====================

    public UserAccountRequest() {
    }

    // ==================== Getters and Setters ====================

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}
