package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.ActivityCreateUpdateDto;
import com.lugialo.donatify.dto.ActivityResponseDto;

import java.util.List;

public interface ActivityService {

    // --- Métodos de Usuário Comum ---
    List<ActivityResponseDto> getAvailableActivities();

    void enrollUserInActivity(Long userId, Long activityId);

    // --- Métodos de Administrador ---
    ActivityResponseDto createActivity(ActivityCreateUpdateDto createDto);

    List<ActivityResponseDto> getAllActivitiesForAdmin();

    ActivityResponseDto getActivityByIdForAdmin(Long id);

    ActivityResponseDto updateActivity(Long id, ActivityCreateUpdateDto updateDto);

    void deleteActivity(Long id);
}
