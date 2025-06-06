package com.lugialo.donatify.repository;

import com.lugialo.donatify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findTop100ByOrderByTotalPointsDesc();

    @Query("SELECT count(u) FROM User u WHERE u.totalPoints > :points")
    long countUsersWithMorePoints(@Param("points") int points);
}
