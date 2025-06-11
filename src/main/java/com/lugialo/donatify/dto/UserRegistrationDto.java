package com.lugialo.donatify.dto;
import lombok.Data;

@Data
public class UserRegistrationDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String address;
    private Long ongId;
}
