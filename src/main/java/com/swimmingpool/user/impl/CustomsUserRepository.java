package com.swimmingpool.user.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.user.UserRowMapper;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserSearchResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomsUserRepository {

    private final EntityManager entityManager;

    public PageResponse<UserSearchResponse> searchUser(UserSearchRequest userSearchRequest) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT u.username as username")
                .append(" , u.role as role, u.address as address, u.created_date as createdDate")
                .append(" , u.modified_date as modifiedDate, u.full_name as fullName")
                .append(" , u.id as id, u.email as email, u.phone as phone, u.active as active")
                .append(" FROM _user u WHERE 1 = 1");
        Map<String, Object> param = new HashMap<>();

        if (StringUtils.hasLength(userSearchRequest.getName())) {
            sqlBuilder.append(" AND (u.username LIKE :name OR u.full_name LIKE :name)");
            param.put("name", "%" + userSearchRequest.getName() + "%");
        }

        if (Objects.nonNull(userSearchRequest.getRole())) {
            sqlBuilder.append(" AND u.role = :role");
            param.put("role", userSearchRequest.getRole().name());
        }
        sqlBuilder.append(" ORDER BY u.created_date DESC");
        return PagingUtil.paginate(sqlBuilder.toString(), param, userSearchRequest, UserRowMapper.userSearchResponseRowMapper())
                .apply(this.entityManager);
    }
}
