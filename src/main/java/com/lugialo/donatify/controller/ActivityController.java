package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.service.ActivityService;
import com.lugialo.donatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    // Endpoint para a tela 'Atividades'
    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getAvailableActivities() {
        List<ActivityResponseDto> activities = activityService.getAvailableActivites();
        return ResponseEntity.ok(activities);
    }

    // Endpoint para a participação do usuário em uma atividade
    public ResponseEntity<?> enrollInActivity(@PathVariable Long activityId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado.");
        }
        // Busca o usuário a partir do email (username) do token
        User user = userService.getUserByEmail(userDetails.getUsername())
                .map(dto -> userService.getUserEntityById(dto.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        try {
            activityService.enrollUserInActivity(user.getId(), activityId);
            return ResponseEntity.ok().body("Inscrição realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao se inscrever na atividade: " + e.getMessage());
        }
    }
}
