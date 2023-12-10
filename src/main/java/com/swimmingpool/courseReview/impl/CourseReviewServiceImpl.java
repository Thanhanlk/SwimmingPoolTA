package com.swimmingpool.courseReview.impl;

import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.CourseRepository;
import com.swimmingpool.courseReview.CourseReview;
import com.swimmingpool.courseReview.CourseReviewRepository;
import com.swimmingpool.courseReview.ICourseReviewService;
import com.swimmingpool.courseReview.request.CreateCourseReviewRequest;
import com.swimmingpool.courseReview.response.CourseReviewDto;
import com.swimmingpool.courseReview.response.CreateCourseReviewResponse;
import com.swimmingpool.user.User;
import com.swimmingpool.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseReviewServiceImpl implements ICourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public List<CourseReviewDto> findByCourseCode(String courseCode) {
        return this.courseReviewRepository.findByCourseCode(courseCode)
                .stream()
                .map(x -> {
                    CourseReviewDto courseReviewDto = new CourseReviewDto();
                    courseReviewDto.setId(x.getReviewId());
                    courseReviewDto.setUsername(x.getCreatedBy());
                    courseReviewDto.setCreatedDate(x.getCreatedDate());
                    courseReviewDto.setContent(x.getComment());
                    courseReviewDto.setStar(x.getRating());
                    this.userRepository.findByUsername(x.getCreatedBy())
                        .map(User::getFullName)
                        .ifPresent(courseReviewDto::setCreatedName);
                    return courseReviewDto;
                }).collect(Collectors.toList());
    }

    @Override
    public CreateCourseReviewResponse save(CreateCourseReviewRequest request) {
        final Course course = this.courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessException(I18nUtil.getMessage("course.id.validate.not-exist", request.getCourseId()), null));
        CourseReview courseReview = new CourseReview();
        courseReview.setCourse(course);
        courseReview.setRating(request.getStar());
        courseReview.setComment(request.getContent());
        this.courseReviewRepository.save(courseReview);
        return CreateCourseReviewResponse.builder()
                .courseReview(courseReview)
                .build();
    }
}
