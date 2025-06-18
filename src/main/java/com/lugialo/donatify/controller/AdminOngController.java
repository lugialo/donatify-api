package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.OngCreateUpdateDto;
import com.lugialo.donatify.dto.OngDto;
import com.lugialo.donatify.service.OngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ongs")
public class AdminOngController {

    @Autowired
    private OngService ongService;

    @PostMapping
    public ResponseEntity<?> createOng(@RequestBody OngCreateUpdateDto ongDto) {
        try {
            OngDto createdOng = ongService.createOng(ongDto);
            return new ResponseEntity<>(createdOng, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<OngDto>> getAllOngs() {
        List<OngDto> ongs = ongService.getAllOngs();
        return ResponseEntity.ok(ongs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOngById(@PathVariable Long id) {
        try {
            OngDto ong = ongService.getOngById(id);
            return ResponseEntity.ok(ong);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOng(@PathVariable Long id, @RequestBody OngCreateUpdateDto ongDto) {
        try {
            OngDto updatedOng = ongService.updateOng(id, ongDto);
            return ResponseEntity.ok(updatedOng);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOng(@PathVariable Long id) {
        try {
            ongService.deleteOng(id);
            return ResponseEntity.ok().body("ONG com ID " + id + " deletada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
