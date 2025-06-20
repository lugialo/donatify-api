package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private long id;
    private String name;
    private String email;
    private String nickname;
    private String role;
    private String ongName;
    private String phone;
    private String address;
    private int totalPoints;
    private LocalDateTime createdAt;

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole().name());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setTotalPoints(user.getTotalPoints());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
