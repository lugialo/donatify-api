package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.ActivityCreateUpdateDto;
import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.model.Activity;
import com.lugialo.donatify.model.ActivityStatus;
import com.lugialo.donatify.model.Enrollment;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.repository.ActivityRepository;
import com.lugialo.donatify.repository.EnrollmentRepository;
import com.lugialo.donatify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    // --- Métodos de Usuário Comum (já existentes) ---

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getAvailableActivities() {
        return activityRepository.findByStatus(ActivityStatus.ACTIVE).stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void enrollUserInActivity(Long userId, Long activityId) {
        // ... (lógica existente)
    }


    // --- MÉTODOS DE ADMINISTRADOR ---

    @Override
    @Transactional
    public ActivityResponseDto createActivity(ActivityCreateUpdateDto createDto) {
        Activity activity = new Activity();
        // Mapeia os dados do DTO para a nova entidade
        activity.setTitle(createDto.getTitle());
        activity.setDescription(createDto.getDescription());
        activity.setPointsValue(createDto.getPointsValue());
        activity.setType(createDto.getType());
        activity.setStatus(createDto.getStatus());
        activity.setStartDate(createDto.getStartDate());
        activity.setEndDate(createDto.getEndDate());
        activity.setLocation(createDto.getLocation());

        Activity savedActivity = activityRepository.save(activity);
        return ActivityResponseDto.fromEntity(savedActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getAllActivitiesForAdmin() {
        // Admin vê todas as atividades, independente do status
        return activityRepository.findAll().stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponseDto getActivityByIdForAdmin(Long id) {
        return activityRepository.findById(id)
                .map(ActivityResponseDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Atividade com ID " + id + " não encontrada."));
    }

    @Override
    @Transactional
    public ActivityResponseDto updateActivity(Long id, ActivityCreateUpdateDto updateDto) {
        Activity activityToUpdate = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atividade com ID " + id + " não encontrada."));

        // Atualiza a entidade com os dados do DTO
        activityToUpdate.setTitle(updateDto.getTitle());
        activityToUpdate.setDescription(updateDto.getDescription());
        activityToUpdate.setPointsValue(updateDto.getPointsValue());
        activityToUpdate.setType(updateDto.getType());
        activityToUpdate.setStatus(updateDto.getStatus());
        activityToUpdate.setStartDate(updateDto.getStartDate());
        activityToUpdate.setEndDate(updateDto.getEndDate());
        activityToUpdate.setLocation(updateDto.getLocation());

        Activity updatedActivity = activityRepository.save(activityToUpdate);
        return ActivityResponseDto.fromEntity(updatedActivity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new IllegalArgumentException("Atividade com ID " + id + " não encontrada, não foi possível deletar.");
        }
        // Ao deletar uma atividade, as inscrições associadas também serão deletadas
        // devido ao 'ON DELETE CASCADE' definido no banco.
        activityRepository.deleteById(id);
    }
}
