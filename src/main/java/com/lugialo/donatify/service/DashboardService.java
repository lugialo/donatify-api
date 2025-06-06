package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.DashboardResponseDto;
import com.lugialo.donatify.dto.RecentActivityDto;
import com.lugialo.donatify.model.EnrollmentStatus;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.repository.EnrollmentRepository;
import com.lugialo.donatify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboardData(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Contar atividades concluídas
        long completedCount = enrollmentRepository.countByUser_IdAndStatus(userId, EnrollmentStatus.COMPLETED);

        // Obter posição no ranking
        // A posição é 1 + (número de pessoas com mais pontos que o usuário)
        long usersAhead = userRepository.countUsersWithMorePoints(user.getTotalPoints());
        int rankingPosition = (int) usersAhead + 1;

        // Obter atividades recentes
        List<RecentActivityDto> recentActivities = enrollmentRepository.findTop5ByUser_IdOrderByEnrollmentDateDesc(userId)
                .stream()
                .map(RecentActivityDto::fromEntity)
                .collect(Collectors.toList());

        // Montar e retornar o DTO
        return DashboardResponseDto.builder()
                .totalPoints(user.getTotalPoints())
                .completedActivities(completedCount)
                .rankingPosition(rankingPosition)
                .recentActivities(recentActivities)
                .build();
    }
}
