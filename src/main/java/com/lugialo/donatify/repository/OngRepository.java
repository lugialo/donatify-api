package com.lugialo.donatify.repository;

import com.lugialo.donatify.model.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {
}
