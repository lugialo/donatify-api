package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.DashboardResponseDto;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.service.DashboardService;
import com.lugialo.donatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado.");
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .map(dto -> userService.getUserEntityById(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no token"));

        DashboardResponseDto dashboardData = dashboardService.getDashboardData(user.getId());
        return ResponseEntity.ok(dashboardData);
    }


}
