package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.EnrollmentDetailDto;
import com.lugialo.donatify.dto.UserProfileUpdateDto;
import com.lugialo.donatify.dto.UserRegistrationDto;
import com.lugialo.donatify.dto.UserResponseDto;
import com.lugialo.donatify.model.EnrollmentStatus;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.service.EnrollmentService;
import com.lugialo.donatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public UserController(UserService userService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            UserResponseDto newUser = userService.registerUser(registrationDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserProfileUpdateDto updateDto) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }

        try {
            // Passa o email do usuário (vindo do token) e os dados de atualização para o serviço
            UserResponseDto updatedUser = userService.updateUserProfile(userDetails.getUsername(), updateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            // Captura erros de validação (ex: senha atual incorreta)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Captura outros erros inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o perfil.");
        }
    }
    
    @GetMapping("/me/enrollments")
    public ResponseEntity<?> getUserEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado.");
        }
        
        try {
            // Obtém o usuário a partir do email (username)
            UserResponseDto user = userService.getUserByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            
            // Busca todas as inscrições do usuário
            List<EnrollmentDetailDto> enrollments = enrollmentService.getUserEnrollments(user.getId());

            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar inscrições: " + e.getMessage());
        }
    }
}
