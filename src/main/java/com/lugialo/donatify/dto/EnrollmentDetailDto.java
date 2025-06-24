package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.Enrollment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnrollmentDetailDto {

    private Long enrollmentId;
    private String enrollmentStatus;
    private LocalDateTime enrollmentDate;
    private LocalDateTime completionDate;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long activityId;
    private String activityTitle;
    private int pointsValue;

    public static EnrollmentDetailDto fromEntity(Enrollment enrollment) {
        EnrollmentDetailDto dto = new EnrollmentDetailDto();
        dto.setEnrollmentId(enrollment.getId());
        dto.setEnrollmentStatus(enrollment.getStatus().name());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setCompletionDate(enrollment.getCompletionDate());

        if (enrollment.getUser() != null) {
            dto.setUserId(enrollment.getUser().getId());
            dto.setUserName(enrollment.getUser().getName());
            dto.setUserEmail(enrollment.getUser().getEmail());
        }

        if (enrollment.getActivity() != null) {
            dto.setActivityId(enrollment.getActivity().getId());
            dto.setActivityTitle(enrollment.getActivity().getTitle());
            dto.setPointsValue(enrollment.getActivity().getPointsValue());
        }

        return dto;
    }
}
