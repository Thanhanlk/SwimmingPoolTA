package com.swimmingpool.user.request;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.user.UserConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class StaffRegisterRequest extends UserRegisterRequest {

    @Override
    @NotBlank(message = "user.address.validate.empty")
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    @NotBlank(message = "user.phone.validate.empty")
    @Pattern(regexp = AppConstant.Validation.RGX_VIETNAM_PHONE, message = "user.phone.validate.pattern")
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    @NotBlank(message = "user.email.validate.empty")
    @Email(message = "user.email.valdiate.pattern")
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public UserConstant.Role getRole() {
        return UserConstant.Role.TEACHER;
    }
}
