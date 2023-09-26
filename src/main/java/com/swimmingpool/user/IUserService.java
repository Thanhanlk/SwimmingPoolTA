package com.swimmingpool.user;

import com.swimmingpool.common.dto.Result;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    Result<UserResponse> registerUser(UserRegisterRequest userRegisterRequest);

}
