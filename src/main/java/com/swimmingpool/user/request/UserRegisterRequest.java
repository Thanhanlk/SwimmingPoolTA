package com.swimmingpool.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swimmingpool.validation.annotation.RepeatPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@RepeatPassword(message = "Mật khẩu không trùng nhau.")
public class UserRegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    private String repeatPassword;

    public String getFullName() {
        return String.format("%s %s", this.lastName, this.firstName);
    }
}
