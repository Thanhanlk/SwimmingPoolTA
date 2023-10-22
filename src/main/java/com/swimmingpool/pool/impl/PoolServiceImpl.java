package com.swimmingpool.pool.impl;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.Pool;
import com.swimmingpool.pool.PoolRepository;
import com.swimmingpool.pool.request.PoolCreationRequest;
import com.swimmingpool.pool.request.PoolSearchRequest;
import com.swimmingpool.pool.response.PoolResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoolServiceImpl implements IPoolService {

    private final PoolRepository poolRepository;
    private final CustomPoolRepository customPoolRepository;

    @Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
    private IAssignmentService assignmentService;

    @Override
    public PageResponse<PoolResponse> searchPool(PoolSearchRequest poolSearchRequest) {
        return this.customPoolRepository.searchPool(poolSearchRequest);
    }

    @Override
    public Pool findByIdThrowIfNotPresent(String poolId) throws ValidationException {
        return this.poolRepository.findById(poolId)
                .orElseThrow(() -> new ValidationException("pool.id.validate.not-exist", poolId));
    }

    @Override
    @Transactional
    public void savePool(PoolCreationRequest creationRequest) {
        Pool pool = new Pool();
        if (StringUtils.hasLength(creationRequest.getId())) {
            pool = this.findByIdThrowIfNotPresent(creationRequest.getId());
        } else {
            Optional<Pool> poolByCode = this.poolRepository.findByCode(creationRequest.getCode());
            if (poolByCode.isPresent()) {
                throw new ValidationException("pool.code.validate.exist", creationRequest.getCode());
            }
            creationRequest.setActive(false);
            pool.setCode(creationRequest.getCode());
            pool.setActive(creationRequest.getActive());
            pool.setNumberOfStudent(creationRequest.getNumberOfStudent());
        }
        pool.setName(creationRequest.getName());
        this.poolRepository.save(pool);
    }

    @Override
    @Cacheable(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_POOL", key = "'EMPTY'", unless = "#result eq null or #result.empty")
    public List<PoolResponse> findAllActive() {
        log.info("get active pool");
        return this.poolRepository.findActivePool().stream()
                .map(pool -> {
                    PoolResponse poolResponse = new PoolResponse();
                    poolResponse.setId(pool.getId());
                    poolResponse.setCode(pool.getCode());
                    poolResponse.setName(pool.getName());
                    return poolResponse;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_POOL", key = "'EMPTY'")
    public void changeStatus(String id) {
        Pool pool = this.findByIdThrowIfNotPresent(id);
        pool.setActive(!pool.getActive());
        this.poolRepository.save(pool);
    }

    @Override
    @Transactional
    @CacheEvict(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_POOL", key = "'EMPTY'")
    public void deletePool(String id) {
        List<Assignment> courseByPoolId = this.assignmentService.findByPoolId(id);
        if (!courseByPoolId.isEmpty()) {
            throw new ValidationException("pool.delete.exist-assignment");
        }
        this.poolRepository.deleteById(id);
    }
}
