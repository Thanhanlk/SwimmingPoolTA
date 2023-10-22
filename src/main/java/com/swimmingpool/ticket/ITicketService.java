package com.swimmingpool.ticket;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.ticket.request.CreateTicketRequest;
import com.swimmingpool.ticket.request.SearchTicketRequest;
import com.swimmingpool.ticket.request.TicketRequest;
import com.swimmingpool.ticket.request.TicketWithReasonRequest;
import com.swimmingpool.ticket.response.TicketResponse;

public interface ITicketService {

    PageResponse<TicketResponse> searchTicket(SearchTicketRequest searchTicketRequest);

    void createTicket(CreateTicketRequest createTicketRequest);

    void approveTicket(TicketRequest ticket);

    void rejectTicket(TicketWithReasonRequest ticket);

    void cancelTicket(TicketWithReasonRequest ticket);
}
