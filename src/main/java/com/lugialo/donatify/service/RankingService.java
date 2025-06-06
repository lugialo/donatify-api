package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.RankingResponseDto;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RankingResponseDto> getTopUsers() {
        List<User> topUsers = userRepository.findTop100ByOrderByTotalPointsDesc();

        AtomicInteger rank = new AtomicInteger(1);
        return topUsers.stream()
                .map(user -> RankingResponseDto.fromEntity(user, rank.getAndIncrement()))
                .collect(Collectors.toList());
    }
}
