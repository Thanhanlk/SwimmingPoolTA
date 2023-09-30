package com.swimmingpool.course;

import com.swimmingpool.course.response.CourseSearchResponse;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class CourseRowMapper {

    public Function<Tuple, CourseSearchResponse> searchCourseMapper() {
        return t -> {
            CourseSearchResponse courseSearchResponse = new CourseSearchResponse();
            courseSearchResponse.setId(t.get("id", String.class));
            courseSearchResponse.setCode(t.get("code", String.class));
            courseSearchResponse.setName(t.get("name", String.class));
            courseSearchResponse.setDiscount(t.get("discount", Integer.class));
            courseSearchResponse.setPrice(t.get("price", BigDecimal.class));
            courseSearchResponse.setNumberOfLesson(t.get("numberOfLesson", Integer.class));
            courseSearchResponse.setCreatedDate(t.get("createdDate", Date.class));
            courseSearchResponse.setModifiedDate(t.get("modifiedDate", Date.class));
            courseSearchResponse.setPoolName(t.get("poolName", String.class));
            courseSearchResponse.setAvatar(t.get("avatar", String.class));
            courseSearchResponse.setActive(t.get("active", Boolean.class));
            courseSearchResponse.setSlug(t.get("slug", String.class));
            return courseSearchResponse;
        };
    }

}
