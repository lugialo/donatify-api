package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.Role;
import lombok.Data;

@Data
public class AdminUserUpdateDto {
    private String name;
    private String nickname;
    private String email;
    private Role role;
    private Long ongId;
}
