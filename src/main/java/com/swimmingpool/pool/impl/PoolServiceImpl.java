package com.swimmingpool.pool.impl;

import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.PoolRepository;
import com.swimmingpool.pool.response.PoolResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoolServiceImpl implements IPoolService {

    private final PoolRepository poolRepository;

    @Override
    @Cacheable(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_POOL", key = "'EMPTY'")
    public List<PoolResponse> findAllActive() {
        log.info("get active pool");
        return this.poolRepository.findActivePool().stream()
                .map(pool -> {
                    PoolResponse poolResponse = new PoolResponse();
                    poolResponse.setId(pool.getId());
                    poolResponse.setCode(pool.getCode());
                    poolResponse.setName(pool.getName());
                    return poolResponse;
                }).toList();
    }
}
