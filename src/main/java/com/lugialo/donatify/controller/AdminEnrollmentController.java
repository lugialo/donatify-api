package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.EnrollmentDetailDto;
import com.lugialo.donatify.model.EnrollmentStatus;
import com.lugialo.donatify.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Lista todas as inscrições. Aceita um filtro opcional por status.
     * Exemplos:
     * GET /api/admin/enrollments
     * GET /api/admin/enrollments?status=COMPLETED
     * GET /api/admin/enrollments?status=ENROLLED
     */
    @GetMapping
    public ResponseEntity<List<EnrollmentDetailDto>> listAllEnrollments(
            @RequestParam(required = false) Optional<EnrollmentStatus> status) {

        List<EnrollmentDetailDto> enrollments = enrollmentService.getAllEnrollments(status);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * Invalida/Cancela uma inscrição.
     */
    @PostMapping("/{enrollmentId}/cancel")
    public ResponseEntity<?> cancelEnrollment(@PathVariable Long enrollmentId) {
        try {
            enrollmentService.cancelEnrollmentByAdmin(enrollmentId);
            return ResponseEntity.ok().body("Inscrição com ID " + enrollmentId + " foi cancelada.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}