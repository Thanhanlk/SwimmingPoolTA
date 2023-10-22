package com.swimmingpool.cart.impl;

import com.swimmingpool.cart.response.CartResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomCartRepository {

    private final EntityManager entityManager;

    public List<CartResponse> getMyCart(String username, List<String> cartIds) {
        String sql = new StringBuilder("SELECT cart.id, c.name, c.price, c.avatar, c.discount")
                .append("  , a.dayOfWeek, a.startTime, a.endTime, a.startDate, c.slug")
                .append("  , a.id as assignmentId, c.id as courseId")
                .append(" FROM Cart cart")
                .append("  JOIN Assignment a ON a.id = cart.assignmentId")
                .append("  JOIN Course c ON c.id = a.courseId")
                .append(" WHERE cart.username = :username")
                .append(!CollectionUtils.isEmpty(cartIds) ? " AND cart.id IN :cartIds" : "")
                .append(" ORDER BY a.dayOfWeek, a.startTime ASC")
                .toString();
        Query query = entityManager.createQuery(sql, Tuple.class);
        if (!CollectionUtils.isEmpty(cartIds)) {
            query.setParameter("cartIds", cartIds);
        }
        query.setParameter("username", username);
        List<Tuple> resultList = query.getResultList();
        return resultList.stream()
                .map(tuple -> {
                    DayOfWeek dayOfWeek = DayOfWeek.of(tuple.get(5, Integer.class));
                    return CartResponse.builder()
                            .cartId(tuple.get(0, String.class))
                            .courseName(tuple.get(1, String.class))
                            .price(tuple.get(2, BigDecimal.class))
                            .courseImage(tuple.get(3, String.class))
                            .discount(tuple.get(4, Integer.class))
                            .dayOfWeek(dayOfWeek)
                            .startTime(tuple.get(6, Time.class))
                            .endTime(tuple.get(7, Time.class))
                            .startDate(tuple.get(8, Date.class))
                            .slug(tuple.get(9, String.class))
                            .assignmentId(tuple.get(10, String.class))
                            .courseId(tuple.get(11, String.class))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
