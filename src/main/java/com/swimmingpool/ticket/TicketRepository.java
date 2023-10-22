package com.swimmingpool.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    @Query("SELECT t FROM Ticket t WHERE t.ticketType = ?1 AND t.objectId = ?2 AND t.status IN ?3")
    Optional<Ticket> findByTicketTypeAndObjectIdAndInStatus(TicketConstant.Type type, String objectId, List<TicketConstant.Status> status);
}
