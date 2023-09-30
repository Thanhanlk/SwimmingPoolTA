package com.swimmingpool.user.request;

import com.swimmingpool.user.UserConstant;
import com.swimmingpool.validation.annotation.RepeatPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@RepeatPassword(message = "user.password.validate.not-match")
public class UserRegisterRequest {

    private String id;

    @NotBlank(message = "user.firstname.validate.empty")
    private String firstName;

    @NotBlank(message = "user.lastname.validate.empty")
    private String lastName;

    @NotBlank(message = "user.username.validate.empty")
    private String username;

    @NotBlank(message = "user.password.validate.empty")
    private String password;

    @NotBlank(message = "user.repeat-password.validate.empty")
    private String repeatPassword;

    private String email;
    private String phone;
    private String address;

    public String getFullName() {
        return String.format("%s %s", this.lastName, this.firstName);
    }

    public UserConstant.Role getRole() {
        return UserConstant.Role.USER;
    }
}
