package com.swimmingpool.assignment.impl;

import com.swimmingpool.assignment.AssignmentRowMapper;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomAssignmentRepository {

    private final EntityManager entityManager;

    public PageResponse<AssignmentCreationResponse> searchAssignment(AssignmentSearchRequest request) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT a.id as id, u.username as username")
                .append(" , u.full_name as fullName, a.day_of_week as dayOfWeek")
                .append(" , a.start_time as startTime, a.end_time as endTime")
                .append(" , c.name as courseName, p.name as poolName, a.created_date as createdDate")
                .append(" , a.modified_date as modifiedDate, a.active as active")
                .append(" FROM _assignment a")
                .append("  JOIN _pool p ON p.id = a.pool_id")
                .append("  JOIN _course c ON c.id = a.course_id")
                .append("  JOIN _user u ON u.id = a.user_id")
                .append(" WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(request.getCodeNameUser())) {
            sqlBuilder.append(" AND (u.username LIKE :codeName OR u.full_name LIKE :codeName)");
            params.put("codeName", "%" + request.getCodeNameUser() + "%");
        }

        if (Objects.nonNull(request.getDay())) {
            sqlBuilder.append(" AND a.day_of_week = :dayOfWeek");
            params.put("dayOfWeek", request.getDay());
        }

        if (Objects.nonNull(request.getStartTime())) {
            sqlBuilder.append(" AND a.start_time = :startTime");
            params.put("startTime", request.getStartTime());
        }

        if (Objects.nonNull(request.getEndTime())) {
            sqlBuilder.append(" AND a.end_time = :endTime");
            params.put("endTime", request.getEndTime());
        }
        sqlBuilder.append(" ORDER BY a.day_of_week, a.start_time");
        return PagingUtil.paginate(sqlBuilder.toString(), params, request, AssignmentRowMapper.assignmentSearchRowMapper())
                .apply(this.entityManager);
    }
}
