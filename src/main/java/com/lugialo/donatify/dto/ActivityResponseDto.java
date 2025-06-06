package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.Activity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityResponseDto {
    private Long id;
    private String title;
    private String description;
    private int pointsValue;
    private String type;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;

    public static ActivityResponseDto fromEntity(Activity activity) {
        ActivityResponseDto dto = new ActivityResponseDto();
        dto.setId(activity.getId());
        dto.setTitle(activity.getTitle());
        dto.setDescription(activity.getDescription());
        dto.setPointsValue(activity.getPointsValue());
        dto.setType(activity.getType().name().toLowerCase());
        dto.setStatus(activity.getStatus().name().toLowerCase());
        dto.setStartDate(activity.getStartDate());
        dto.setEndDate(activity.getEndDate());
        dto.setLocation(activity.getLocation());

        return dto;
    }
}
