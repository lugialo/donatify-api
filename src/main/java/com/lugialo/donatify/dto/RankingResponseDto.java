package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingResponseDto {
    private int rank;
    private String name;
    private int totalPoints;

    public static RankingResponseDto fromEntity(User user, int rank) {
        return new RankingResponseDto(
                rank,
                user.getName(),
                user.getTotalPoints()
        );
    }
}
