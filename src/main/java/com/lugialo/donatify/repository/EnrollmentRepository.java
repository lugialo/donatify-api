package com.lugialo.donatify.repository;

import com.lugialo.donatify.model.Enrollment;
import com.lugialo.donatify.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Conta quantas inscrições um usuário tem com um status específico
    long countByUser_IdAndStatus(Long userId, EnrollmentStatus status);

    // Busca as últimas inscrições de um usuário
    List<Enrollment> findTop5ByUser_IdOrderByEnrollmentDateDesc(Long userId);

    // Verifica se um usuário já está inscrito em uma atividade
    boolean existsByUser_IdAndActivity_Id(Long userId, Long activityId);

    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    // Busca inscrições de um usuário com um determinado status
    List<Enrollment> findByUser_IdAndStatus(Long userId, EnrollmentStatus status);

    // Busca todas as inscrições de um usuário
    List<Enrollment> findByUser_Id(Long userId);
}
