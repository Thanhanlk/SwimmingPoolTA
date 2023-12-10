package com.swimmingpool.courseReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    @Query("SELECT c FROM CourseReview c WHERE c.course.code = ?1")
    List<CourseReview> findByCourseCode(String courseId);
}
