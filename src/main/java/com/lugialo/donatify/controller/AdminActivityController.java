package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.ActivityCreateUpdateDto;
import com.lugialo.donatify.dto.ActivityResponseDto;
import com.lugialo.donatify.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/activities") // Rota protegida para admins
public class AdminActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(@RequestBody ActivityCreateUpdateDto createDto) {
        ActivityResponseDto newActivity = activityService.createActivity(createDto);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getAllActivities() {
        List<ActivityResponseDto> activities = activityService.getAllActivitiesForAdmin();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getActivityById(@PathVariable Long id) {
        try {
            ActivityResponseDto activity = activityService.getActivityByIdForAdmin(id);
            return ResponseEntity.ok(activity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody ActivityCreateUpdateDto updateDto) {
        try {
            ActivityResponseDto updatedActivity = activityService.updateActivity(id, updateDto);
            return ResponseEntity.ok(updatedActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        try {
            activityService.deleteActivity(id);
            return ResponseEntity.ok().body("Atividade com ID " + id + " deletada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
