package com.swimmingpool.course.impl;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.assignment.response.AvailableAssignmentResponse;
import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.common.util.NumberUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.CourseRepository;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.*;
import com.swimmingpool.courseReview.CourseReviewRepository;
import com.swimmingpool.image.ImageConstant;
import com.swimmingpool.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final CustomCourseRepositoryImpl customCourseRepositoryImpl;
    private final CourseReviewRepository courseReviewRepository;

    @Setter(onMethod_ = { @Autowired }, onParam_ = @Lazy)
    private ImageService imageService;

    @Setter(onMethod_ = { @Autowired }, onParam_ = @Lazy)
    private IAssignmentService assignmentService;

    @Override
    public void save(Course course) {
        this.courseRepository.save(course);
    }

    @Override
    @Cacheable(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_COURSE", key = "'EMPTY'", unless = "#result eq null or #result.empty")
    public List<Course> findActiveCourse() {
        return this.courseRepository.findActiveCourse();
    }

    @Override
    public Course findByIdThrowIfNotPresent(String id) throws ValidationException {
        return this.courseRepository.findById(id)
                .orElseThrow(() -> new ValidationException(I18nUtil.getMessage("course.id.validate.not-exist", id)));
    }

    @Override
    public PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest) {
        final PageResponse<CourseSearchResponse> response = this.customCourseRepositoryImpl.searchCourse(courseSearchRequest);
        this.fetchAvgStar(response.getItems());
        return response;
    }

    @Override
    @Transactional
    public CourseCreationResponse saveCourse(CourseCreationRequest courseCreationRequest) throws IOException {
        log.info("save course: {}", courseCreationRequest);
        Course course = new Course();
        if (StringUtils.hasLength(courseCreationRequest.getId())) {
            course = this.findByIdThrowIfNotPresent(courseCreationRequest.getId());
            courseCreationRequest.setCode(course.getCode());
        } else {
            Optional<Course> courseByCode = this.courseRepository.findByCode(courseCreationRequest.getCode());
            if (courseByCode.isPresent()) {
                throw new ValidationException("course.code.validate.exist", courseCreationRequest.getCode());
            }
        }

        course.setCode(courseCreationRequest.getCode());
        course.setName(courseCreationRequest.getName());
        course.setPrice(courseCreationRequest.getPrice());
        course.setNumberOfLesson(courseCreationRequest.getNumberOfLesson());
        course.setShortDescription(courseCreationRequest.getShortDescription());
        course.setDescription(courseCreationRequest.getDescription());
        course.setDiscount(courseCreationRequest.getDiscount());
        if (Objects.isNull(course.getDiscount())) {
            course.setDiscount(0);
        }
        course.setActive(false);
        course.setIsShowHome(courseCreationRequest.getIsShowHome());
        course.setSlug(courseCreationRequest.getSlug());

        if (Objects.nonNull(courseCreationRequest.getAvatar())) {
            MultipartFile avatar = courseCreationRequest.getAvatar();
            String avatarUrl = this.imageService.upload(avatar, String.format("%s-%s", course.getCode(), course.getName()), ImageConstant.Type.COURSE);
            if (StringUtils.hasLength(avatarUrl)) {
                course.setAvatar(avatarUrl);
            }
        }
        this.courseRepository.save(course);
        return new CourseCreationResponse();
    }

    @Override
    @Transactional
    @CacheEvict(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_COURSE", key = "'EMPTY'")
    public void changeStatus(String id) {
        Course course = this.findByIdThrowIfNotPresent(id);
        course.setActive(!course.getActive());
        this.courseRepository.save(course);
    }

    @Transactional
    @Override
    @CacheEvict(cacheManager = "redisCacheManager", cacheNames = "ACTIVE_COURSE", key = "'EMPTY'")
    public void deleteCourse(String id) {
        List<Assignment> assignmentByCourseId = this.assignmentService.findByCourseId(id);
        if (!assignmentByCourseId.isEmpty()) {
            throw new ValidationException("course.delete.exist-assignment");
        }
        this.courseRepository.deleteById(id);
    }

    @Override
    public List<CourseImageDTO> getCourseImage(int limit) {
        return this.customCourseRepositoryImpl.getCourseImage(limit);
    }

    @Override
    public List<CourseResponse> getBestSeller(int limit) {
        final List<CourseResponse> bestSeller = this.customCourseRepositoryImpl.getBestSeller(limit);
        this.fetchAvgStar(bestSeller);
        return bestSeller;
    }

    @Override
    public List<CourseResponse> getNewest(int limit) {
        final List<CourseResponse> newest = this.customCourseRepositoryImpl.getNewest(limit);
        this.fetchAvgStar(newest);
        return newest;
    }

    @Override
    public List<CourseResponse> getHotSales(int limit) {
        final List<CourseResponse> hotSales = this.customCourseRepositoryImpl.getHotSales(limit);
        this.fetchAvgStar(hotSales);
        return hotSales;
    }

    @Override
    public CourseDetailResponse getDetailProduct(String courseCode, int limitRelatedCourse) {
        Course course = this.courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new ValidationException("course.code.validate.not-exist", courseCode));
        if (!course.getActive()) {
            throw new ValidationException("course.code.validate.not-exist", courseCode);
        }
        List<AvailableAssignmentResponse> activeAssignmentByCourseId = this.assignmentService.findAvailableAssignmentByCourseId(course.getId());
        return CourseDetailResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .image(course.getAvatar())
                .shortDescription(course.getShortDescription())
                .description(course.getDescription())
                .assignments(activeAssignmentByCourseId)
                .slug(course.getSlug())
                .discount(course.getDiscount())
                .price(course.getPrice())
                .build();
    }

    private void fetchAvgStar(List<? extends CourseResponse> courseResponses) {
        final List<String> ids = courseResponses.stream()
                .map(CourseResponse::getId)
                .toList();
        final Map<Object, Map<String, Object>> avgStarByCourseId = this.courseReviewRepository.getAvgStarByCourseId(ids).stream()
                .collect(Collectors.toMap(x -> x.get("courseId"), Function.identity()));
        courseResponses.forEach(courseResponse -> {
            final Map<String, Object> map = avgStarByCourseId.get(courseResponse.getId());
            if (Objects.nonNull(map)) {
                final int avgRating = NumberUtil.toInt(map.get("avgStar"));
                final int count = NumberUtil.toInt(map.get("count"));
                courseResponse.setAvgStar(avgRating);
                courseResponse.setNumberOfReview(count);
            }
        });
    }
}
