package com.swimmingpool.user.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swimmingpool.user.UserConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchResponse extends UserResponse {

    private UserConstant.Role role;

    public UserSearchResponse(String username, boolean active, UserConstant.Role role) {
        super(username, active, role);
        this.role = role;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }
}
