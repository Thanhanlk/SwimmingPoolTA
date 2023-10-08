package com.swimmingpool.user.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.User;
import com.swimmingpool.user.UserRepository;
import com.swimmingpool.user.request.ChangePassword;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserResponse;
import com.swimmingpool.user.response.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final CustomsUserRepository customsUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(UserResponse::new)
                .orElseThrow(() -> new UsernameNotFoundException(I18nUtil.getMessage("user.login.fail")));
    }

    @Override
    public User findByIdThrowIfNotPresent(String id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("user.id.validate.not-exist", id));
    }

    @Transactional
    @CacheEvict(cacheManager = "redisCacheManager", cacheNames = "TEACHER", key = "'EMPTY'", beforeInvocation = true, condition = "#userRegisterRequest instanceof com.swimmingpool.user.request.StaffRegisterRequest")
    public Result<UserResponse> registerUser(UserRegisterRequest userRegisterRequest) {
        log.info("register user: {}", userRegisterRequest);
        User user = new User();
        if (StringUtils.hasLength(userRegisterRequest.getId())) {
            user = this.findByIdThrowIfNotPresent(userRegisterRequest.getId());
            userRegisterRequest.setUsername(user.getUsername());
        } else {
            this.userRepository.findByUsername(userRegisterRequest.getUsername().toLowerCase())
                    .ifPresent(u -> {
                        throw new ValidationException("user.username.validate.exist");
                    });
        }

        if (Objects.nonNull(userRegisterRequest.getPhone())) {
            this.userRepository.findByPhone(userRegisterRequest.getPhone())
                    .filter(x -> !x.getId().equals(userRegisterRequest.getId()))
                    .ifPresent(u -> {
                        throw new ValidationException("user.phone.validate.exist", u.getPhone());
                    });
        }

        if (Objects.nonNull(userRegisterRequest.getEmail())) {
            this.userRepository.findByEmail(userRegisterRequest.getEmail())
                    .filter(x -> !x.getId().equals(userRegisterRequest.getId()))
                    .ifPresent(u -> {
                        throw new ValidationException("user.email.valdiate.exist", u.getEmail());
                    });
        }
        user.setUsername(userRegisterRequest.getUsername().toLowerCase());
        user.setActive(true);
        user.setRole(userRegisterRequest.getRole());
        user.setPassword(this.passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setFullName(userRegisterRequest.getFullName());
        user.setPhone(userRegisterRequest.getPhone());
        user.setEmail(userRegisterRequest.getEmail());
        user.setAddress(userRegisterRequest.getAddress());
        user = this.userRepository.save(user);
        UserResponse userResponse = new UserResponse(user);
        return Result.success(userResponse, I18nUtil.getMessage("user.register.success"));
    }

    @Override
    public PageResponse<UserSearchResponse> searchUser(UserSearchRequest userSearchRequest) {
        return this.customsUserRepository.searchUser(userSearchRequest);
    }

    @Override
    public List<UserSearchResponse> findUser(UserSearchRequest userSearchRequest) {
        return this.customsUserRepository.searchUserNotPaging(userSearchRequest);
    }

    @Override
    @Transactional
    public void changeStatus(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("user.id.validate.not-exist", id));
        user.setActive(!user.getActive());
        this.userRepository.save(user);
    }

    @Override
    public UserResponse getCurrentUserThrowIfNot() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(x -> x instanceof UserResponse)
                .map(UserResponse.class::cast)
                .orElseThrow();
    }

    @Override
    public void updateUser(UserResponse userUpdateRequest) {
        UserResponse userResponse = this.getCurrentUserThrowIfNot();
        User u = this.findByIdThrowIfNotPresent(userResponse.getId());
        u.setAddress(userUpdateRequest.getAddress());
        u.setPhone(userUpdateRequest.getPhone());
        u.setEmail(userUpdateRequest.getEmail());
        u.setFullName(userUpdateRequest.getFullName());
        this.userRepository.save(u);
    }

    @Override
    public void changePassword(ChangePassword changePassword) {
        UserResponse userResponse = this.getCurrentUserThrowIfNot();
        if (!this.passwordEncoder.matches(changePassword.getCurrentPassword(), userResponse.getPassword())) {
            throw new ValidationException("change-password.current-password.validate.not-match");
        }
        User u = this.findByIdThrowIfNotPresent(userResponse.getId());
        String newPassword = this.passwordEncoder.encode(changePassword.getNewPassword());
        u.setPassword(newPassword);
        this.userRepository.save(u);
    }
}
