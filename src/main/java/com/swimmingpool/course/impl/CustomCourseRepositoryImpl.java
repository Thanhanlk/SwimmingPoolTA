package com.swimmingpool.course.impl;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.course.CourseRowMapper;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseImageDTO;
import com.swimmingpool.course.response.CourseResponse;
import com.swimmingpool.course.response.CourseSearchResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl {

    private final EntityManager entityManager;

    public PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest) {

        StringBuilder sqlBuilder = new StringBuilder("select c.id as id, c.code as code, c.name as name, c.created_date as createdDate")
                .append(" , c.modified_date as modifiedDate, c.price as price, c.number_of_lesson as numberOfLesson")
                .append(" , c.discount as discount")
                .append(" , c.avatar as avatar, c.active as active, c.slug as slug")
                .append(" from _course c")
                .append(" where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasLength(courseSearchRequest.getCodeName())) {
            sqlBuilder.append(" AND (c.code LIKE :codeName OR c.name LIKE :codeName)");
            params.put("codeName", "%" + courseSearchRequest.getCodeName() + "%");
        }

        if (courseSearchRequest.getActive() != null) {
            sqlBuilder.append(" AND (c.active = :active)");
            params.put("active", courseSearchRequest.getActive());
        }
        sqlBuilder.append(" ORDER BY c.created_date DESC");
        return PagingUtil.paginate(sqlBuilder.toString(), params, courseSearchRequest, CourseRowMapper.searchCourseMapper())
                .apply(this.entityManager);
    }

    public List<CourseImageDTO> getCourseImage(int limit) {
        String sql = new StringBuilder("SELECT c.id, c.name, c.code, c.avatar, c.slug FROM Course c")
                .append(" WHERE c.active = true AND c.isShowHome = true")
                .toString();
        TypedQuery<Tuple> query = entityManager.createQuery(sql, Tuple.class);
        query.setMaxResults(limit);
        List<Tuple> resultList = query.getResultList();
        return resultList.stream()
                .map(tuple -> CourseImageDTO.builder()
                        .id(tuple.get(0, String.class))
                        .name(tuple.get(1, String.class))
                        .code(tuple.get(2, String.class))
                        .imageUrl(tuple.get(3, String.class))
                        .slug(tuple.get(4, String.class))
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getBestSeller(int limit) {
        String sql = new StringBuilder("SELECT c.id, c.code, c.name, c.avatar, c.price, c.slug , count(1) as count")
                .append(" FROM _course c")
                .append("  JOIN _assignment a ON c.id = a.course_id")
                .append("  JOIN _order o ON a.id = o.assignment_id")
                .append(" WHERE c.active = true")
                .append(" GROUP BY c.id")
                .append(" ORDER BY count DESC")
                .toString();
        return this.getCourseInUserHome(sql, limit);
    }

    public List<CourseResponse> getNewest(int limit) {
        String sql = new StringBuilder("SELECT c.id, c.code, c.name, c.avatar, c.price, c.slug")
                .append(" FROM _course c")
                .append(" WHERE c.active = true AND DATEDIFF(now(), c.created_date) <= ?1")
                .append(" ORDER BY c.created_date DESC")
                .toString();
        return this.getCourseInUserHome(sql, limit, AppConstant.DATE_INDICATE_NEWEST);
    }

    public List<CourseResponse> getHotSales(int limit) {
        String sql = new StringBuilder("SELECT p.id, p.code, p.name, p.avatar, p.price, p.slug")
                .append(" FROM _course p")
                .append(" WHERE p.active = true AND p.discount  >= ?1")
                .append(" ORDER BY p.discount DESC")
                .toString();
        return this.getCourseInUserHome(sql, limit, AppConstant.PERCENT_INDICATE_HOT_SALE);
    }

    private List<CourseResponse> getCourseInUserHome(String sql, int limit, Object... params) {
        Function<Tuple, CourseResponse> mapper = tuple -> CourseResponse.builder()
                .id(tuple.get(0, String.class))
                .code(tuple.get(1, String.class))
                .name(tuple.get(2, String.class))
                .image(tuple.get(3, String.class))
                .price(tuple.get(4, BigDecimal.class))
                .slug(tuple.get(5, String.class))
                .build();
        return this.getCourseInUserHome(sql, limit, mapper, params);
    }

    private List<CourseResponse> getCourseInUserHome(String sql, int limit, Function<Tuple, CourseResponse> mapper, Object...params) {
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        if (Objects.nonNull(params)) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        query.setMaxResults(limit);
        List<Tuple> resultList = query.getResultList();
        return resultList.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
