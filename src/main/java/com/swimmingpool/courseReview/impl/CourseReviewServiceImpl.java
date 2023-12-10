package com.swimmingpool.courseReview.impl;

import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.CourseRepository;
import com.swimmingpool.courseReview.CourseReview;
import com.swimmingpool.courseReview.CourseReviewRepository;
import com.swimmingpool.courseReview.ICourseReviewService;
import com.swimmingpool.courseReview.request.CreateCourseReviewRequest;
import com.swimmingpool.courseReview.request.UpdateCourseReviewRequest;
import com.swimmingpool.courseReview.response.CourseReviewDto;
import com.swimmingpool.courseReview.response.CreateCourseReviewResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.User;
import com.swimmingpool.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseReviewServiceImpl implements ICourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final IUserService userService;

    @Override
    @Transactional(readOnly = true)
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
                    courseReviewDto.setModifiedDate(x.getModifiedDate());
                    courseReviewDto.setCourseId(x.getCourse().getId());
                    courseReviewDto.setUpdated(x.isUpdated());
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

    @Override
    public CreateCourseReviewResponse update(UpdateCourseReviewRequest request) {
        final CourseReview courseReview = this.courseReviewRepository.findByIdAndFetchCourse(request.getCourseReviewId())
                .filter(x -> this.userService.getCurrentUserThrowIfNot().getUsername().equals(x.getCreatedBy()))
                .filter(x -> !x.isUpdated())
                .orElseThrow(() -> new BusinessException(I18nUtil.getMessage("course-review.id.validate.not-found", request.getCourseReviewId()), null));
        courseReview.setComment(request.getContent());
        courseReview.setRating(request.getStar());
        courseReview.setUpdated(true);
        this.courseReviewRepository.save(courseReview);
        return CreateCourseReviewResponse.builder()
                .courseReview(courseReview)
                .build();
    }
}
