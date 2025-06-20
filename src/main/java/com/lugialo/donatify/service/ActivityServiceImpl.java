package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.ActivityCreateUpdateDto;
import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.model.*;
import com.lugialo.donatify.repository.ActivityRepository;
import com.lugialo.donatify.repository.EnrollmentRepository;
import com.lugialo.donatify.repository.OngRepository;
import com.lugialo.donatify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private OngRepository ongRepository;

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
        // 1. Verifica se o usuário já está inscrito para evitar duplicatas
        if (enrollmentRepository.existsByUser_IdAndActivity_Id(userId, activityId)) {
            throw new IllegalStateException("Você já está inscrito nesta atividade.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Atividade não encontrada."));


        // 2. Verifica se a atividade não foi cancelada ou encerrada.
        if (activity.getStatus() != ActivityStatus.ACTIVE) {
            throw new IllegalStateException("Não é possível se inscrever em uma atividade que não está ativa.");
        }

        // 3. Verifica se a data de início da atividade já passou.
        // Consideramos a data de início como o prazo final para inscrição.
        if (activity.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("O prazo de inscrição para esta atividade já encerrou.");
        }


        // Se todas as verificações passarem, cria a inscrição.
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setActivity(activity);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);
    }



    // --- MÉTODOS DE ADMINISTRADOR ---

    @Override
    @Transactional
    public ActivityResponseDto createActivity(ActivityCreateUpdateDto createDto) {

        Ong ong = ongRepository.findById(createDto.getOngId())
                .orElseThrow(() -> new IllegalArgumentException("ONG com ID " + createDto.getOngId() + " não encontrada."));

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
        activity.setOng(ong);

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

        if (updateDto.getTitle() != null) {
            activityToUpdate.setTitle(updateDto.getTitle());
        }
        if (updateDto.getDescription() != null) {
            activityToUpdate.setDescription(updateDto.getDescription());
        }
        if (updateDto.getPointsValue() > 0) { // int não pode ser nulo, então verificamos se um valor válido foi enviado
            activityToUpdate.setPointsValue(updateDto.getPointsValue());
        }
        if (updateDto.getType() != null) {
            activityToUpdate.setType(updateDto.getType());
        }
        if (updateDto.getStatus() != null) {
            activityToUpdate.setStatus(updateDto.getStatus());
        }
        if (updateDto.getStartDate() != null) {
            activityToUpdate.setStartDate(updateDto.getStartDate());
        }
        if (updateDto.getEndDate() != null) {
            activityToUpdate.setEndDate(updateDto.getEndDate());
        }
        if (updateDto.getLocation() != null) {
            activityToUpdate.setLocation(updateDto.getLocation());
        }

        if (updateDto.getOngId() != null) {
            // Se foi enviado, verifica se é diferente da ONG já existente na atividade
            if (!updateDto.getOngId().equals(activityToUpdate.getOng().getId())) {
                // Se for diferente, busca a nova ONG e atualiza a referência
                Ong novaOng = ongRepository.findById(updateDto.getOngId())
                        .orElseThrow(() -> new IllegalArgumentException("ONG com ID " + updateDto.getOngId() + " não encontrada para associação."));
                activityToUpdate.setOng(novaOng);
            }
            // Se o ongId enviado for o mesmo que já está na atividade, não fazemos nada.
        }

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
