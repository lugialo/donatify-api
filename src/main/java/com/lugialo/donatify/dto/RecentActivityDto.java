package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.Enrollment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentActivityDto {
    private String title;
    private LocalDateTime date;
    private int points;
    private String status;

    public static RecentActivityDto fromEntity(Enrollment enrollment) {
        RecentActivityDto dto = new RecentActivityDto();
        dto.setTitle(enrollment.getActivity().getTitle());
        dto.setDate(enrollment.getEnrollmentDate());
        dto.setPoints(enrollment.getActivity().getPointsValue());
        dto.setStatus(enrollment.getActivity().getStatus().name().toLowerCase());
        return dto;
    }
}
