package com.swimmingpool.pool;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.pool.request.PoolCreationRequest;
import com.swimmingpool.pool.request.PoolSearchRequest;
import com.swimmingpool.pool.response.PoolResponse;

import java.util.List;

public interface IPoolService {
    List<PoolResponse> findAllActive();

    void savePool(PoolCreationRequest creationRequest);

    Pool findByIdThrowIfNotPresent(String poolId) throws ValidationException;

    PageResponse<PoolResponse> searchPool(PoolSearchRequest poolSearchRequest);

    void changeStatus(String id);

    void deletePool(String id);
}
