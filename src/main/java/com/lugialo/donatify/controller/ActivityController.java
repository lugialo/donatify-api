package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.service.ActivityService;
import com.lugialo.donatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // Você vai precisar do seu serviço de usuário aqui para pegar o ID
    @Autowired
    private com.lugialo.donatify.service.UserService userService;

    // Endpoint para a tela 'Atividades'
    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getAvailableActivities() {
        List<ActivityResponseDto> activities = activityService.getAvailableActivities();
        return ResponseEntity.ok(activities);
    }

    // Endpoint para o botão 'Participar'
    @PostMapping("/{activityId}/enroll")
    public ResponseEntity<?> enrollInActivity(@PathVariable Long activityId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado.");
        }
        // Busca o usuário do nosso banco de dados a partir do email (username) do token
        com.lugialo.donatify.model.User user = userService.getUserByEmail(userDetails.getUsername())
                .map(dto -> userService.getUserEntityById(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no token"));

        try {
            activityService.enrollUserInActivity(user.getId(), activityId);
            return ResponseEntity.ok().body("Inscrição realizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
