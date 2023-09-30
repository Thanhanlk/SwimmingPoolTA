package com.swimmingpool.pool;

import com.swimmingpool.pool.response.PoolResponse;

import java.util.List;

public interface IPoolService {
    List<PoolResponse> findAllActive();
}
