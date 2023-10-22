package com.swimmingpool.timetable.impl;

import com.swimmingpool.timetable.response.TimetableResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTimetableRepository {

    private final EntityManager entityManager;

    public List<TimetableResponse> getTeacherTimeTeacher(String username) {
        String sql = new StringBuilder("SELECT a.id, a.dayOfWeek")
                .append(" , a.startTime, a.endTime")
                .append(" , p.code, p.name, c.code, c.name")
                .append(" , u.fullName, u.username")
                .append(" FROM Assignment a")
                .append(" JOIN Course c ON c.id = a.courseId")
                .append(" JOIN Pool p ON p.id = a.poolId")
                .append(" JOIN User u ON u.id = a.userId")
                .append(" WHERE u.username = :username AND a.active = true")
                .append(" ORDER BY a.dayOfWeek, a.startTime ASC")
                .toString();
        return this.mappingTimetableResponse(sql, username);
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
        return this.mappingTimetableResponse(sql, username);
    }

    public List<TimetableResponse> mappingTimetableResponse(String sql, String username) {
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
