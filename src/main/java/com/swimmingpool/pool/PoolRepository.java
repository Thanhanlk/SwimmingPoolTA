package com.swimmingpool.pool;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolRepository extends JpaRepository<Pool, String> {
}
