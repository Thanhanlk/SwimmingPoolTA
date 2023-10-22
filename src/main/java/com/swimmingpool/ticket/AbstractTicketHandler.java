package com.swimmingpool.ticket;

import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.ticket.request.CreateTicketRequest;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.response.UserResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Setter(onMethod_ = @Autowired)
public abstract class AbstractTicketHandler implements TicketHandler {

    protected static final List<TicketConstant.Status> PENDING_AND_APPROVED_STATUS = List.of(TicketConstant.Status.PENDING, TicketConstant.Status.APPROVED);

    protected TicketRepository ticketRepository;
    protected IUserService userService;

    public void checkCreator(String username) {
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        if (!userResponse.getUsername().equals(username)) {
            throw new ValidationException("ticket.unauthorization");
        }
    }

    @Override
    public void create(CreateTicketRequest createTicketRequest) {
        Optional<Ticket> byTicketTypeAndObjectId = this.ticketRepository.findByTicketTypeAndObjectIdAndInStatus(
                createTicketRequest.getType(),
                createTicketRequest.getObjectId(),
                PENDING_AND_APPROVED_STATUS
        );
        if (byTicketTypeAndObjectId.isPresent()) {
            throw new ValidationException("ticket.exist");
        }
        this.validateCreateTicket(createTicketRequest);
        Ticket ticket = new Ticket();
        ticket.setTicketType(createTicketRequest.getType());
        ticket.setObjectId(createTicketRequest.getObjectId());
        ticket.setStatus(TicketConstant.Status.PENDING);
        ticket.setDescription(createTicketRequest.getDescription());
        this.ticketRepository.save(ticket);
    }

    public abstract void validateCreateTicket(CreateTicketRequest createTicketRequest);

    @Override
    public void approve(Ticket ticket) {
        if (!TicketConstant.Status.PENDING.equals(ticket.getStatus())) {
            throw new ValidationException("ticket.status.validate.invalid");
        }
        ticket.setStatus(TicketConstant.Status.APPROVED);
        this.ticketRepository.save(ticket);
    }

    @Override
    public void reject(Ticket ticket, String reason) {
        if (!TicketConstant.Status.PENDING.equals(ticket.getStatus())) {
            throw new ValidationException("ticket.status.validate.invalid");
        }
        ticket.setStatus(TicketConstant.Status.REJECT);
        ticket.setAdminReason(reason);
        this.ticketRepository.save(ticket);
    }

    @Override
    public void cancel(Ticket ticket, String reason) {
        if (!TicketConstant.Status.PENDING.equals(ticket.getStatus())) {
            throw new ValidationException("ticket.status.validate.invalid");
        }
        this.checkCreator(ticket.getCreatedBy());
        ticket.setStatus(TicketConstant.Status.CANCEL);
        ticket.setTeacherReason(reason);
        this.ticketRepository.save(ticket);
    }
}
