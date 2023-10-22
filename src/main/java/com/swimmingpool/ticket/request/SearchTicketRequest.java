package com.swimmingpool.ticket.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class SearchTicketRequest extends PageRequest {
    private Date fromDate;
    private Date toDate;
    private String creator;
}
