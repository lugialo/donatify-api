package com.lugialo.donatify.controller;

import com.lugialo.donatify.dto.RankingResponseDto;
import com.lugialo.donatify.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingResponseDto>> getRanking() {
        return ResponseEntity.ok(rankingService.getTopUsers());
    }
}
