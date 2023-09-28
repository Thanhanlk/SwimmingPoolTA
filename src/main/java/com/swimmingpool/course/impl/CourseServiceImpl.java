package com.swimmingpool.course.impl;

import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.CourseRepository;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.response.CourseCreationResponse;
import com.swimmingpool.pool.Pool;
import com.swimmingpool.pool.PoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final PoolRepository poolRepository;

    @Override
    @Transactional
    public Result<CourseCreationResponse> saveCourse(CourseCreationRequest courseCreationRequest) {
        log.info("create course: {}", courseCreationRequest);
        Optional<Course> courseByCode = this.courseRepository.findByCode(courseCreationRequest.getCode());
        if (courseByCode.isPresent()) {
            throw new ValidationException("course.code.validate.exist", courseCreationRequest.getCode());
        }

        Optional<Pool> poolById = this.poolRepository.findById(courseCreationRequest.getPoolId());
        if (poolById.isEmpty()) {
            throw new ValidationException("pool.id.validate.not-exist");
        }

        Course course = new Course();
        course.setCode(courseCreationRequest.getCode());
        course.setName(courseCreationRequest.getName());
        course.setPrice(courseCreationRequest.getPrice());
        course.setNumberOfLesson(courseCreationRequest.getNumberOfLesson());
        course.setNumberOfStudent(courseCreationRequest.getNumberOfStudent());
        course.setPoolId(courseCreationRequest.getPoolId());
        course.setShortDescription(courseCreationRequest.getShortDescription());
        course.setDescription(courseCreationRequest.getDescription());

        // todo: handle upload course's images

        this.courseRepository.save(course);
        return null;
    }
}
