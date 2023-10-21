package com.swimmingpool.ticket.request;

import com.swimmingpool.ticket.TicketConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketRequest {

    @NotNull
    private TicketConstant.Type type;

    @NotBlank
    private String objectId;

    @NotBlank
    private String description;
}
