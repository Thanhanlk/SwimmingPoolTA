package com.swimmingpool.assignment.impl;

import com.swimmingpool.assignment.AssignmentRowMapper;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import com.swimmingpool.assignment.response.AvailableAssignmentResponse;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.DateUtil;
import com.swimmingpool.common.util.PagingUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
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
                .append(" , a.modified_date as modifiedDate, u.id as userId, a.start_date as startDate")
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
            params.put("dayOfWeek", request.getDay().getValue());
        }

        if (StringUtils.hasLength(request.getStartTime())) {
            sqlBuilder.append(" AND a.start_time >= :startTime");
            params.put("startTime", DateUtil.strToTime(request.getStartTime()));
        }

        if (StringUtils.hasLength(request.getEndTime())) {
            sqlBuilder.append(" AND a.end_time <= :endTime");
            params.put("endTime", DateUtil.strToTime(request.getEndTime()));
        }

        if (StringUtils.hasLength(request.getPoolId())) {
            sqlBuilder.append(" AND a.pool_id = :poolId");
            params.put("poolId", request.getPoolId());
        }

        if (StringUtils.hasLength(request.getCourseId())) {
            sqlBuilder.append(" AND a.course_id = :courseId");
            params.put("courseId", request.getCourseId());
        }
        sqlBuilder.append(" ORDER BY a.day_of_week, a.start_time");
        return PagingUtil.paginate(sqlBuilder.toString(), params, request, AssignmentRowMapper.assignmentSearchRowMapper())
                .apply(this.entityManager);
    }

    public List<AvailableAssignmentResponse> findAvailableAssignment(String courseId) {
        String sql = new StringBuilder("SELECT a.id, a.dayOfWeek, a.startTime, a.endTime, p.numberOfStudent, count(o.id) FROM Assignment a")
                .append("  LEFT JOIN Order o ON o.assignmentId = a.id")
                .append("  JOIN Pool p ON p.id = a.poolId")
                .append(" WHERE a.active = true AND a.startDate > CURRENT_DATE AND a.courseId = :courseId")
                .append(" GROUP BY a.id")
                .toString();

        TypedQuery<Tuple> query = this.entityManager.createQuery(sql, Tuple.class);
        query.setParameter("courseId", courseId);
        List<Tuple> resultList = query.getResultList();
        return resultList.stream()
                .map(tuple -> {
                    var assignmentResponse = new AvailableAssignmentResponse();
                    Integer dayOfWeek = tuple.get(1, Integer.class);
                    assignmentResponse.setId(tuple.get(0, String.class));
                    assignmentResponse.setDayOfWeek(DayOfWeek.of(dayOfWeek));
                    assignmentResponse.setStartTime(tuple.get(2, Time.class));
                    assignmentResponse.setEndTime(tuple.get(3, Time.class));
                    assignmentResponse.setMaxStudent(tuple.get(4, Integer.class));
                    assignmentResponse.setRegisterStudent(tuple.get(5, Long.class));
                    return assignmentResponse;
                })
                .toList();
    }
}
