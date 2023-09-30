package com.swimmingpool.course.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.course.CourseRowMapper;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseSearchResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl {

    private final EntityManager entityManager;

    public PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest) {

        StringBuilder sqlBuilder = new StringBuilder("select c.id as id, c.code as code, c.name as name, c.created_date as createdDate")
                .append(" , c.modified_date as modifiedDate, c.price as price, c.number_of_lesson as numberOfLesson")
                .append(" , c.number_of_student as numberOfStudent, c.discount as discount, p.name as poolName")
                .append(" , c.avatar as avatar, c.active as active, c.slug as slug")
                .append(" from _course c")
                .append("  join _pool p ON p.id = c.pool_id")
                .append(" where 1 = 1");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasLength(courseSearchRequest.getCodeName())) {
            sqlBuilder.append(" AND (c.code LIKE :codeName OR c.name LIKE :codeName)");
            params.put("codeName", "%" + courseSearchRequest.getCodeName() + "%");
        }
        sqlBuilder.append(" ORDER BY c.created_date DESC");
        return PagingUtil.paginate(sqlBuilder.toString(), params, courseSearchRequest, CourseRowMapper.searchCourseMapper())
                .apply(this.entityManager);
    }
}
