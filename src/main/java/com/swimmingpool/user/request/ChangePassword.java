package com.swimmingpool.user.request;

import com.swimmingpool.validation.annotation.RepeatPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@RepeatPassword(
        fields = {"newPassword", "repeatPassword"},
        message = "user.password.validate.not-match"
)
public class ChangePassword {

    @NotBlank(message = "change-password.current-password.validate.empty")
    private String currentPassword;

    @NotBlank(message = "change-password.new-password.validate.empty")
    private String newPassword;

    @NotBlank(message = "change-password.repeat-password.validate.empty")
    private String repeatPassword;
}
