package com.carshop.oto_shop.dto.user;

import com.carshop.oto_shop.enums.Gender;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class UserRequest {
    private String  fullName;

    private LocalDate dob;

    private Gender gender;

    private String phone;

    private String address;

    private MultipartFile avatarFile;

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
