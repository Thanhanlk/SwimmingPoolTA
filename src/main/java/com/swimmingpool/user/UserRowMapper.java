package com.swimmingpool.user;

import com.swimmingpool.user.response.UserSearchResponse;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class UserRowMapper {

    public Function<Tuple, UserSearchResponse> userSearchResponseRowMapper() {
        return (tuple) -> {
            String username = tuple.get("username", String.class);
            String role = tuple.get("role", String.class);
            Boolean active = tuple.get("active", Boolean.class);
            UserSearchResponse userResponse = new UserSearchResponse(username, active, UserConstant.Role.valueOf(role));
            userResponse.setAddress(tuple.get("address", String.class));
            userResponse.setCreatedDate(tuple.get("createdDate", Date.class));
            userResponse.setModifiedDate(tuple.get("modifiedDate", Date.class));
            userResponse.setFullName(tuple.get("fullName", String.class));
            userResponse.setId(tuple.get("id", String.class));
            userResponse.setEmail(tuple.get("email", String.class));
            userResponse.setPhone(tuple.get("phone", String.class));
            return userResponse;
        };
    }
}
