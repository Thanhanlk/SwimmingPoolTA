package com.swimmingpool.order.request;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.order.OrderConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private String uuid;

    @NotBlank(message = "order.first-name.validate.empty")
    private String firstName;

    @NotBlank(message = "order.last-name.validate.empty")
    private String lastName;

    @NotBlank(message = "order.phone.validate.empty")
    @Pattern(regexp = AppConstant.Validation.RGX_VIETNAM_PHONE, message = "order.phone.validate.pattern")
    private String phone;

    @NotNull(message = "order.method.validate.empty")
    private OrderConstant.MethodPayment method;

    @NotEmpty(message = "order.card-id.validate.empty")
    private List<String> cartId;

    private String createdBy;

    private String paymentId;

    public String getFullName() {
        return String.format("%s %s", this.lastName, this.firstName);
    }
}
