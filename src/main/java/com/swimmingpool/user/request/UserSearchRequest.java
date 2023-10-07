package com.swimmingpool.user.request;

import com.swimmingpool.common.dto.PageRequest;
import com.swimmingpool.user.UserConstant;
import lombok.Data;

@Data
public class UserSearchRequest extends PageRequest {
    private String name;
    private UserConstant.Role role;
    private Boolean active;
}
