package com.swimmingpool.ticket;

import com.swimmingpool.ticket.model.AdditionalInfo;
import com.swimmingpool.ticket.request.CreateTicketRequest;

public interface TicketHandler {

    void create(CreateTicketRequest createTicketRequest);

    void approve(Ticket ticket);

    void cancel(Ticket ticket, String reason);

    void reject(Ticket ticket, String reason);

    AdditionalInfo getAdditionalInfo(String objectId);
}
