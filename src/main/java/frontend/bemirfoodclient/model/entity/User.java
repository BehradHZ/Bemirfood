package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.dto.UserDto;

public class User {

    private String fullName;
    private String mobile;
    private String email;
    private UserRole role;
    private String address;
    private String photo;
    private BankInfo bankInfo;
    private String password;

    public User(String fullName, String mobile, String role, String email, String photo,
                String address, BankInfo bankInfo) {
        this.fullName = fullName;
        this.mobile = mobile;
        if (role.equals("admin")) {
            this.role = UserRole.ADMIN;
        } else if (role.equals("buyer")) {
            this.role = UserRole.CUSTOMER;
        } else if (role.equals("seller")) {
            this.role = UserRole.SELLER;
        } else if (role.equals("courier")) {
            this.role = UserRole.DELIVERY;
        }
        this.email = email;
        this.photo = photo;
        this.address = address;
        this.bankInfo = bankInfo;
        this.password = "";
    }

    public static User UserDtoToUser(UserDto userDto) {
        return new User(
                userDto.getFullName(),
                userDto.getPhone(),
                userDto.getRole(),
                userDto.getEmail(),
                userDto.getProfileImageBase64(),
                userDto.getAddress(),
                userDto.getBankInfo()
        );
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserRole getRole() {
        return role;
    }

    public String getRoleAsString() {
        return switch (role) {
            case ADMIN -> "admin";
            case CUSTOMER -> "buyer";
            case SELLER -> "seller";
            case DELIVERY -> "courier";
        };

    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
