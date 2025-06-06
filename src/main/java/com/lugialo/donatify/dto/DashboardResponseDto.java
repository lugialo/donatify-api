package com.lugialo.donatify.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardResponseDto {
    private int totalPoints;
    private long completedActivities;
    private int rankingPosition;
    private List<RecentActivityDto> recentActivities;
}
