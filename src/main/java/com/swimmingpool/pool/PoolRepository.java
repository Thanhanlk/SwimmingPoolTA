package com.swimmingpool.pool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PoolRepository extends JpaRepository<Pool, String> {

    @Query("SELECT p FROM Pool p WHERE p.active = true")
    List<Pool> findActivePool();

    Optional<Pool> findByCode(String code);
}
