package frontend.bemirfoodclient.model.dto;

import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.User;

public class UserDto {
    String full_name;
    String phone;
    String email;
    String role;
    String address;
    String profileImageBase64;
    Bank_info bank_info;
    String password;

    public UserDto(User user) {
        this.full_name = user.getFull_name();
        this.email = user.getEmail();
        this.phone = user.getMobile();
        this.role = user.getRole().toString();
        this.address = user.getAddress();
        this.profileImageBase64 = user.getPhoto();
        this.bank_info = user.getBank_info();
        this.password = user.getPassword();
    }

    public UserDto(String full_name, String phone, String email, String role, String address, String profileImageBase64,
                   String bank_name, String account_number) {
        this.full_name = full_name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.profileImageBase64 = profileImageBase64;
        this.bank_info = new Bank_info(bank_name, account_number);
        this.password = "";
    }

    public UserDto(String full_name, String phone, String email, String role, String address, String profileImageBase64,
                   String bank_name, String account_number, String password) {
        this.full_name = full_name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.profileImageBase64 = profileImageBase64;
        this.bank_info = new Bank_info(bank_name, account_number);
        this.password = password;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImageBase64() {
        return profileImageBase64;
    }

    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }

    public Bank_info getBank_info() {
        return bank_info;
    }

    public void setBank_info(Bank_info bank_info) {
        this.bank_info = bank_info;
    }

    public String getPassword() {
        return password;
    }
}