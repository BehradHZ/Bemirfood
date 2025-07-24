package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.dto.UserDto;

public class User {

    private Long id;
    private String full_name;
    private String mobile;
    private String email;
    private UserRole role;
    private String address;
    private String photo;
    private Bank_info bank_info;
    private String password;

    public User(String fullName, String mobile, String role, String email, String photo,
                String address, Bank_info bank_info) {
        this.full_name = fullName;
        this.mobile = mobile;
        if (role.equals("admin")) {
            this.role = UserRole.ADMIN;
        } else if (role.equals("buyer")) {
            this.role = UserRole.BUYER;
        } else if (role.equals("seller")) {
            this.role = UserRole.SELLER;
        } else if (role.equals("courier")) {
            this.role = UserRole.COURIER;
        }
        this.email = email;
        this.photo = photo;
        this.address = address;
        this.bank_info = bank_info;
        this.password = "";
    }

    public User(String fullName, String mobile, String role, String email, String photo,
                String address, Bank_info bank_info, String password) {
        this.full_name = fullName;
        this.mobile = mobile;
        if (role.equals("admin")) {
            this.role = UserRole.ADMIN;
        } else if (role.equals("buyer")) {
            this.role = UserRole.BUYER;
        } else if (role.equals("seller")) {
            this.role = UserRole.SELLER;
        } else if (role.equals("courier")) {
            this.role = UserRole.COURIER;
        }
        this.email = email;
        this.photo = photo;
        this.address = address;
        this.bank_info = bank_info;
        this.password = password;
    }

    public User(String mobile) {
        this.mobile = mobile;
    }

    public User() {

    }

    public static User UserDtoToUser(UserDto userDto) {
        return new User(
                userDto.getFullName(),
                userDto.getPhone(),
                userDto.getRole(),
                userDto.getEmail(),
                userDto.getProfileImageBase64(),
                userDto.getAddress(),
                userDto.getBank_info(),
                userDto.getPassword()
        );
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
            case BUYER -> "buyer";
            case SELLER -> "seller";
            case COURIER -> "courier";
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

    public Bank_info getBank_info() {
        return bank_info;
    }

    public void setBank_info(Bank_info bank_info) {
        this.bank_info = bank_info;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
