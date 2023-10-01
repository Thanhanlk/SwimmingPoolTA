package com.swimmingpool.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {

    @Query("SELECT a FROM Assignment a" +
            " WHERE a.poolId = ?1" +
            " AND (?2 BETWEEN a.startTime AND a.endTime" +
            " OR ?3 BETWEEN a.startTime AND a.endTime)" +
            " AND a.dayOfWeek = ?4")
    Optional<Assignment> findByPoolIdAndStartTimeBetweenAndDayOfWeek(String poolId, Time startTime, Time endTime, Integer dayOfWeek);

    @Query("SELECT a FROM Assignment a" +
            " WHERE a.userId = ?1" +
            " AND (?2 BETWEEN a.startTime AND a.endTime" +
            " OR ?3 BETWEEN a.startTime AND a.endTime)" +
            " AND a.dayOfWeek = ?4")
    Optional<Assignment> findByUserIdAndStartTimeBetweenAndDayOfWeek(String userId, Time startTime, Time endTime, Integer dayOfWeek);

    @Query("SELECT a FROM Assignment a WHERE a.userId = ?1 ORDER BY a.dayOfWeek, a.startTime")
    List<Assignment> findByUserId(String userId);

    @Modifying
    @Query("DELETE FROM Assignment a WHERE a.id NOT IN (?1) AND a.userId = ?2")
    void deleteAllNotIds(List<String> ids, String userId);

    @Modifying
    void deleteByUserId(String userId);
}