package com.swimmingpool.pool.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.pool.PoolRowMapper;
import com.swimmingpool.pool.request.PoolSearchRequest;
import com.swimmingpool.pool.response.PoolResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomPoolRepository {

    private final EntityManager entityManager;

    public PageResponse<PoolResponse> searchPool(PoolSearchRequest request) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT p.id as id, p.code as code")
                .append(" , p.name as name, p.active as active")
                .append(" , p.created_date as createdDate, p.modified_date as modifiedDate")
                .append(" , p.number_of_student as numberOfStudent")
                .append("  FROM _pool p")
                .append(" WHERE 1 = 1");
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.hasLength(request.getCodeName())) {
            sqlBuilder.append(" AND (p.code LIKE :codeName OR p.name LIKE :codeName)");
            param.put("codeName", "%" + request.getCodeName() + "%");
        }
        sqlBuilder.append(" ORDER BY p.created_date DESC");
        return PagingUtil.paginate(sqlBuilder.toString(), param, request, PoolRowMapper.poolSearchRowMapper())
                .apply(this.entityManager);
    }
}
