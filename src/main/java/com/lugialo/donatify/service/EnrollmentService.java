package com.lugialo.donatify.service;

import com.lugialo.donatify.model.*;
import com.lugialo.donatify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}