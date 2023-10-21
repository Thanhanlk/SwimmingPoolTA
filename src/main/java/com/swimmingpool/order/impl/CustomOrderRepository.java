package com.swimmingpool.order.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.order.OrderRowMapper;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;
import com.swimmingpool.order.response.TimetableResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {

    private final EntityManager entityManager;

    public PageResponse<OrderSearchResponse> searchOrder(OrderSearchRequest orderSearchRequest) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT o.id as id")
                .append("  , p.name as poolName, c.name as courseName")
                .append("  , a.day_of_week as day, a.start_time as startTime, a.end_time as endTime")
                .append("  , o.full_name as fullName, o.phone as phone")
                .append("  , o.created_date as createdDate, o.status as status")
                .append("  , o.method_payment as methodPayment, o.price as total")
                .append("  , a.start_date as startDate, o.discount as discount")
                .append(" FROM _order o")
                .append("  JOIN _assignment a ON a.id = o.assignment_id")
                .append("  JOIN _course c ON c.id = a.course_id")
                .append("  JOIN _pool p ON p.id = a.pool_id")
                .append(" WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasLength(orderSearchRequest.getId())) {
            sqlBuilder.append(" AND o.id = :id");
            params.put("id", orderSearchRequest.getId());
        }

        if (StringUtils.hasLength(orderSearchRequest.getFullName())) {
            sqlBuilder.append(" AND o.full_name LIKE :fullName");
            params.put("fullName", "%" + orderSearchRequest.getFullName() + "%");
        }

        if (Objects.nonNull(orderSearchRequest.getMethodPayment())) {
            sqlBuilder.append(" AND o.method_payment = :methodPayment");
            params.put("methodPayment", orderSearchRequest.getMethodPayment().name());
        }

        if (StringUtils.hasLength(orderSearchRequest.getPhone())) {
            sqlBuilder.append(" AND o.phone = :phone");
            params.put("phone", orderSearchRequest.getPhone());
        }

        if (Objects.nonNull(orderSearchRequest.getStatus())) {
            sqlBuilder.append(" AND o.status = :status");
            params.put("status", orderSearchRequest.getStatus().name());
        }

        if (Objects.nonNull(orderSearchRequest.getFromDate())) {
            sqlBuilder.append(" AND o.created_date >= :fromDate");
            params.put("fromDate", orderSearchRequest.getFromDate());
        }

        if (Objects.nonNull(orderSearchRequest.getToDate())) {
            sqlBuilder.append(" AND o.created_date <= :toDate");
            params.put("toDate", orderSearchRequest.getToDate());
        }

        if (Objects.nonNull(orderSearchRequest.getCreatedBy())) {
            sqlBuilder.append(" AND o.created_by <= :createdBy");
            params.put("createdBy", orderSearchRequest.getCreatedBy());
        }

        sqlBuilder.append(" ORDER BY o.created_date DESC");

        return PagingUtil.paginate(sqlBuilder.toString(), params, orderSearchRequest, OrderRowMapper.orderSearchRowMapper())
                .apply(this.entityManager);
    }

    public List<TimetableResponse> getUserTimetable(String username) {
        String sql = new StringBuilder("SELECT a.id, a.dayOfWeek, a.startTime, a.endTime")
                .append(" ,p.code, p.name, c.code, c.name")
                .append(" ,u.fullName, u.username")
                .append(" FROM Order o")
                .append(" JOIN Assignment a ON o.assignmentId = a.id")
                .append(" JOIN Pool p ON p.id = a.poolId")
                .append(" JOIN Course c ON c.id = a.courseId")
                .append(" JOIN User u ON u.id = a.userId")
                .append(" WHERE o.createdBy = :username")
                .append("  AND a.active = true AND a.startDate < CURRENT_DATE AND o.status = 'PAID'")
                .append(" ORDER BY a.dayOfWeek, a.startTime ASC")
                .toString();
        TypedQuery<Tuple> query = this.entityManager.createQuery(sql, Tuple.class);
        query.setParameter("username", username);
        List<Tuple> resultList = query.getResultList();
        return resultList.stream()
                .map(tuple -> {
                    return TimetableResponse.builder()
                            .assignmentId(tuple.get(0, String.class))
                            .dayOfWeek(tuple.get(1, Integer.class))
                            .startTime(tuple.get(2, Time.class))
                            .endTime(tuple.get(3, Time.class))
                            .poolCode(tuple.get(4, String.class))
                            .poolName(tuple.get(5, String.class))
                            .courseCode(tuple.get(6, String.class))
                            .courseName(tuple.get(7, String.class))
                            .teacher(tuple.get(8, String.class))
                            .teacherUsername(tuple.get(9, String.class))
                            .build();
                })
                .toList();
    }
}
