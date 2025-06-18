package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.ActivityStatus;
import com.lugialo.donatify.model.ActivityType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityCreateUpdateDto {
    private String title;
    private String description;
    private int pointsValue;
    private ActivityType type;
    private ActivityStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
}
