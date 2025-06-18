package com.lugialo.donatify.repository;

import com.lugialo.donatify.model.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {
    Optional<Ong> findByNameIgnoreCase(String name);
}
