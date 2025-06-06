package com.lugialo.donatify.controller;

import com.lugialo.donatify.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * @param enrollmentId O ID da inscrição a ser concluída.
     */
    @PostMapping("/{enrollmentId}/complete")
    public ResponseEntity<?> completeEnrollment(@PathVariable Long enrollmentId) {
        try {
            enrollmentService.completeEnrollment(enrollmentId);
            return ResponseEntity.ok().body("Atividade concluída e pontos creditados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}