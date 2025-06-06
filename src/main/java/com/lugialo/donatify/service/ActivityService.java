package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lugialo.donatify.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Retorna todas as atividades disponíveis (ativas)
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getAvailableActivites() {
        return activityRepository.findByStatus(ActivityStatus.ACTIVE).stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Inscreve um usuário em uma atividade
    @Transactional
    public void enrollUserInActivity(Long userId, Long activityId) {
        if (enrollmentRepository.existsByUser_IdAndActivity_Id(userId, activityId)) {
            throw new IllegalArgumentException("Usuário já está inscrito nesta atividade.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Atividade não encontrada."));

        if (activity.getStatus() != ActivityStatus.ACTIVE) {
            throw new IllegalArgumentException("Atividade não está ativa.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setActivity(activity);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);

    }
}
