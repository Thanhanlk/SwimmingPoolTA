package com.swimmingpool.user;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.user.request.ChangePassword;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserResponse;
import com.swimmingpool.user.response.UserSearchResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    User findByIdThrowIfNotPresent(String id);

    Result<UserResponse> registerUser(UserRegisterRequest userRegisterRequest);

    PageResponse<UserSearchResponse> searchUser(UserSearchRequest userSearchRequest);

    List<UserSearchResponse> findUser(UserSearchRequest userSearchRequest);

    default List<UserSearchResponse> findStaffUser(UserSearchRequest userSearchRequest) {
        userSearchRequest.setRole(UserConstant.Role.TEACHER);
        userSearchRequest.setActive(true);
        return this.findUser(userSearchRequest);
    }

    default List<UserSearchResponse> findCustomerUser(UserSearchRequest userSearchRequest) {
        userSearchRequest.setRole(UserConstant.Role.USER);
        return this.findUser(userSearchRequest);
    }

    void changeStatus(String id);

    UserResponse getCurrentUserThrowIfNot();

    void updateUser(UserResponse userUpdateRequest);

    void changePassword(ChangePassword changePassword);
}
