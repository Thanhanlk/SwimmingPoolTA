package com.swimmingpool.ticket.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketWithReasonRequest extends TicketRequest {
    @NotBlank
    private String reason;
}
