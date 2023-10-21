package com.swimmingpool.ticket.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.ticket.*;
import com.swimmingpool.ticket.factory.TicketHandlerFactory;
import com.swimmingpool.ticket.request.CreateTicketRequest;
import com.swimmingpool.ticket.request.SearchTicketRequest;
import com.swimmingpool.ticket.request.TicketRequest;
import com.swimmingpool.ticket.request.TicketWithReasonRequest;
import com.swimmingpool.ticket.response.TicketResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.UserConstant;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final TicketRepository ticketRepository;
    private final TicketHandlerFactory ticketHandlerFactory;
    private final CustomTicketRepository customTicketRepository;
    private final IUserService userService;

    @Override
    public PageResponse<TicketResponse> searchTicket(SearchTicketRequest searchTicketRequest) {
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        if (UserConstant.Role.TEACHER.equals(userResponse.getRole())) {
            searchTicketRequest.setCreator(userResponse.getUsername());
        }
        return this.customTicketRepository.searchTicket(searchTicketRequest);
    }

    @Override
    @Transactional
    public void createTicket(CreateTicketRequest createTicketRequest) {
        TicketHandler instance = this.ticketHandlerFactory.getInstance(createTicketRequest.getType());
        instance.create(createTicketRequest);
    }

    public Ticket findByTypeAndObjectId(String id) {
        return this.ticketRepository.findById(id)
                .orElseThrow(() -> new ValidationException("ticket.id.validate.not-found"));
    }

    @Override
    public void approveTicket(TicketRequest ticket) {
        Ticket t = this.findByTypeAndObjectId(ticket.getId());
        TicketHandler instance = this.ticketHandlerFactory.getInstance(t.getTicketType());
        instance.approve(t);
    }

    @Override
    public void rejectTicket(TicketWithReasonRequest ticket) {
        Ticket t = this.findByTypeAndObjectId(ticket.getId());
        TicketHandler instance = this.ticketHandlerFactory.getInstance(t.getTicketType());
        instance.reject(t, ticket.getReason());
    }

    @Override
    public void cancelTicket(TicketWithReasonRequest ticket) {
        Ticket t = this.findByTypeAndObjectId(ticket.getId());
        TicketHandler instance = this.ticketHandlerFactory.getInstance(t.getTicketType());
        instance.cancel(t, ticket.getReason());
    }
}
