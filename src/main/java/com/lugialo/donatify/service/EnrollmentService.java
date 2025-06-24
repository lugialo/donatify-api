package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.EnrollmentDetailDto;
import com.lugialo.donatify.model.*;
import com.lugialo.donatify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Marca uma inscrição como concluída e concede os pontos ao usuário.
     */
    @Transactional
    public void completeEnrollment(Long enrollmentId) {
        // Busca a inscrição no banco ou lança um erro se não encontrar.
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Inscrição com ID " + enrollmentId + " não encontrada."));

        // Verifica se a inscrição já não foi concluída para evitar pontos duplicados.
        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Esta inscrição já foi concluída.");
        }

        // Pega o usuário e a atividade associados à inscrição.
        User user = enrollment.getUser();
        Activity activity = enrollment.getActivity();
        int pointsToEarn = activity.getPointsValue();

        // Atualiza a inscrição.
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletionDate(LocalDateTime.now());
        enrollmentRepository.save(enrollment);

        // Adiciona os pontos ao total do usuário.
        user.setTotalPoints(user.getTotalPoints() + pointsToEarn);
        userRepository.save(user);

        // Cria um registro histórico na tabela 'points'.
        String description = "Pontos ganhos pela conclusão da atividade: " + activity.getTitle();
        Point newPointTransaction = new Point(user, activity, pointsToEarn, description);
        pointRepository.save(newPointTransaction);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDetailDto> getAllEnrollments(Optional<EnrollmentStatus> status) {
        List<Enrollment> enrollments;
        if (status.isPresent()) {
            // Se um status foi fornecido, filtra por ele
            enrollments = enrollmentRepository.findByStatus(status.get());
        } else {
            // Senão, busca todas as inscrições
            enrollments = enrollmentRepository.findAll();
        }

        return enrollments.stream()
                .map(EnrollmentDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDetailDto> getUserEnrollments(Long userId, EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentRepository.findByUser_IdAndStatus(userId, status);
        return enrollments.stream()
                .map(EnrollmentDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDetailDto> getUserEnrollments(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUser_Id(userId);
        return enrollments.stream()
                .map(EnrollmentDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelEnrollmentByAdmin(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Inscrição com ID " + enrollmentId + " não encontrada."));

        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            User user = enrollment.getUser();
            int pointsToDeduct = enrollment.getActivity().getPointsValue();

            user.setTotalPoints(Math.max(0, user.getTotalPoints() - pointsToDeduct));
            userRepository.save(user);

        }

        enrollment.setStatus(EnrollmentStatus.CANCELED_BY_ADMIN); // Reutilizando status existente.
        enrollmentRepository.save(enrollment);
    }
}