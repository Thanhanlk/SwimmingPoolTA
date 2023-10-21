package com.swimmingpool.ticket.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketRequest {

    @NotBlank
    private String id;
}
