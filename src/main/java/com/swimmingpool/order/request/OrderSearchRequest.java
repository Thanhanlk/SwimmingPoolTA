package com.swimmingpool.order.request;

import com.swimmingpool.common.dto.PageRequest;
import com.swimmingpool.order.OrderConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OrderSearchRequest extends PageRequest {

    private String id;
    private String code;
    private String fullName;
    private OrderConstant.MethodPayment methodPayment;
    private String phone;
    private OrderConstant.Status status;
    private String address;
    private String createdBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date toDate;
}
