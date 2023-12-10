package com.swimmingpool.courseReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    @Query("SELECT c FROM CourseReview c WHERE c.course.code = ?1")
    List<CourseReview> findByCourseCode(String courseId);

    @Query("SELECT c FROM CourseReview c JOIN FETCH c.course WHERE c.reviewId = ?1")
    Optional<CourseReview> findByIdAndFetchCourse(Long reviewId);
}
