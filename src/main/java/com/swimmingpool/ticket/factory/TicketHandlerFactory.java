package com.swimmingpool.ticket.factory;

import com.swimmingpool.ticket.TicketConstant;
import com.swimmingpool.ticket.TicketHandler;
import com.swimmingpool.ticket.impl.handler.CloseCourseTicketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketHandlerFactory {

    private final CloseCourseTicketHandler closeCourseTicketHandler;

    public TicketHandler getInstance(TicketConstant.Type type) {
        return switch (type) {
            case CLOSE_COURSE -> closeCourseTicketHandler;
            default -> throw new UnsupportedOperationException();
        };
    }

}
