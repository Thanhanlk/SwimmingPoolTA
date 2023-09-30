package com.swimmingpool.user;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserResponse;
import com.swimmingpool.user.response.UserSearchResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    User findByIdThrowIfNotPresent(String id);

    Result<UserResponse> registerUser(UserRegisterRequest userRegisterRequest);

    PageResponse<UserSearchResponse> searchUser(UserSearchRequest userSearchRequest);

    void changeStatus(String id);
}
