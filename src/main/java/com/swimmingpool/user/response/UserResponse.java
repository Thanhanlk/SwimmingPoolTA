package com.swimmingpool.user.response;

import com.swimmingpool.user.UserConstant;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserResponse extends User {
    private String id;
    private String fullName;
    private String phone;
    private String address;
    private String email;
    private UserConstant.Role role;

    public UserResponse(com.swimmingpool.user.User user) {
        super(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}
