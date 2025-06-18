package com.lugialo.donatify.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDto {

    private String name;
    private String nickname;
    private String phone;
    private String address;

    // Campos para alteração de senha (opcionais)
    private String currentPassword;
    private String newPassword;
}
