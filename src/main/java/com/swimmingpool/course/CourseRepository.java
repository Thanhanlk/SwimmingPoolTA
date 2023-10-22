package com.swimmingpool.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCode(String code);

    @Query("SELECT c FROM Course c WHERE c.active = true")
    List<Course> findActiveCourse();
}
