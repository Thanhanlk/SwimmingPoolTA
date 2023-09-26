package com.swimmingpool.user;

import com.swimmingpool.common.dto.Result;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(UserResponse::new)
                .orElseThrow(() -> new UsernameNotFoundException("Tên đăng nhập hoặc mật khẩu không chính xác"));
    }

    public Result<UserResponse> registerUser(UserRegisterRequest userRegisterRequest) {
        log.info("register user: {}", userRegisterRequest);
        this.userRepository.findByUsername(userRegisterRequest.getUsername().toLowerCase())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
                });
        User user = new User();
        user.setRole(UserConstant.Role.USER);
        user.setActive(true);
        user.setUsername(userRegisterRequest.getUsername().toLowerCase());
        user.setPassword(this.passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setFullName(userRegisterRequest.getFullName());
        user = this.userRepository.save(user);
        UserResponse userResponse = new UserResponse(user);
        return Result.success(userResponse, "Đăng ký thành công");
    }
}
