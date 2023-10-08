package com.swimmingpool.user.response;

import com.swimmingpool.user.UserConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Getter
@Setter
public class UserResponse extends User {
    private String id;
    private String fullName;
    private String phone;
    private String address;
    private String email;
    private UserConstant.Role role;
    private Date createdDate;
    private Date modifiedDate;
    private String username;

    public UserResponse() {
        super(" ", "", new ArrayList<>());
    }

    public UserResponse(com.swimmingpool.user.User user) {
        super(user.getUsername(), user.getPassword(), user.getActive(), true, true, true, roleToAuthority(user.getRole()));
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdDate = user.getCreatedDate();
        this.modifiedDate = user.getModifiedDate();
        this.username = user.getUsername();
    }

    public UserResponse(String username, boolean active, UserConstant.Role role) {
        super(username, "", active, true, true, true, roleToAuthority(role));
        this.username = username;
    }

    public static List<? extends GrantedAuthority> roleToAuthority(UserConstant.Role...roles) {
        return Arrays.stream(roles)
                .map(UserConstant.Role::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public UserResponse(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
    }
}
