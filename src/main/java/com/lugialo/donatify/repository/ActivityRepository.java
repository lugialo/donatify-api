package com.lugialo.donatify.repository;

import com.lugialo.donatify.model.Activity;
import com.lugialo.donatify.model.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // Busca todas as atividades com um status específico
    List<Activity> findByStatus(ActivityStatus status);
}
