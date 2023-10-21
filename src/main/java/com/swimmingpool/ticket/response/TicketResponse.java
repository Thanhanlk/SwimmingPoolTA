package com.swimmingpool.ticket.response;

import com.swimmingpool.ticket.TicketConstant;
import com.swimmingpool.ticket.model.AdditionalInfo;
import lombok.Data;

import java.util.Date;

@Data
public class TicketResponse {
    private String id;
    private TicketConstant.Type ticketType;
    private String teacherReason;
    private String adminReason;
    private String objectId;
    private TicketConstant.Status status;
    private String description;
    private String createdBy;
    private Date createdDate;
    private AdditionalInfo additionalInfo;
}
