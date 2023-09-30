package com.swimmingpool.course.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.CourseRepository;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseCreationResponse;
import com.swimmingpool.course.response.CourseSearchResponse;
import com.swimmingpool.image.ImageConstant;
import com.swimmingpool.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final CustomCourseRepositoryImpl customCourseRepositoryImpl;

    @Setter(onMethod_ = { @Autowired })
    private ImageService imageService;

    @Override
    public Course findByIdThrowIfNotPresent(String id) throws ValidationException {
        return this.courseRepository.findById(id)
                .orElseThrow(() -> new ValidationException(I18nUtil.getMessage("course.id.validate.not-exist", id)));
    }

    @Override
    public PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest) {
        return this.customCourseRepositoryImpl.searchCourse(courseSearchRequest);
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
        course.setNumberOfStudent(courseCreationRequest.getNumberOfStudent());
        course.setShortDescription(courseCreationRequest.getShortDescription());
        course.setDescription(courseCreationRequest.getDescription());
        course.setDiscount(courseCreationRequest.getDiscount());
        course.setActive(false);
        course.setIsShowHome(courseCreationRequest.getIsShowHome());
        course.setSlug(courseCreationRequest.getSlug());

        // todo: handle upload course's images
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
    public void changeStatus(String id) {
        Course course = this.findByIdThrowIfNotPresent(id);
        course.setActive(!course.getActive());
        this.courseRepository.save(course);
    }
}
