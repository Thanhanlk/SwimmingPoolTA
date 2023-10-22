package com.swimmingpool.ticket.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.PagingUtil;
import com.swimmingpool.ticket.Ticket;
import com.swimmingpool.ticket.TicketConstant;
import com.swimmingpool.ticket.factory.TicketHandlerFactory;
import com.swimmingpool.ticket.model.AdditionalInfo;
import com.swimmingpool.ticket.request.SearchTicketRequest;
import com.swimmingpool.ticket.response.TicketResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.response.UserResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomTicketRepository {

    private final EntityManager entityManager;
    private final TicketHandlerFactory ticketHandlerFactory;

    public PageResponse<TicketResponse> searchTicket(SearchTicketRequest searchTicketRequest) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT t.id, t.ticket_type, t.created_by, t.created_date, t.admin_reason, t.teacher_reason, t.object_id, t.status")
                .append(" FROM _ticket t WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (Objects.nonNull(searchTicketRequest.getFromDate())) {
            sqlBuilder.append(" AND t.created_date >= :fromDate");
            params.put("fromDate", searchTicketRequest.getFromDate());
        }

        if (Objects.nonNull(searchTicketRequest.getToDate())) {
            sqlBuilder.append(" AND t.created_date <= :toDate");
            params.put("toDate", searchTicketRequest.getToDate());
        }

        if (Objects.nonNull(searchTicketRequest.getCreator())) {
            sqlBuilder.append(" AND t.created_by = :creator");
            params.put("creator", searchTicketRequest.getCreator());
        }
        sqlBuilder.append(" ORDER BY t.created_date DESC");
        return PagingUtil.paginate(sqlBuilder.toString(), params, searchTicketRequest, tuple -> {
            String type = tuple.get(1, String.class);
            String status = tuple.get(7, String.class);
            TicketResponse ticket = new TicketResponse();
            ticket.setId(tuple.get(0, String.class));
            ticket.setTicketType(TicketConstant.Type.valueOf(type));
            ticket.setCreatedBy(tuple.get(2, String.class));
            ticket.setCreatedDate(tuple.get(3, Date.class));
            ticket.setAdminReason(tuple.get(4, String.class));
            ticket.setTeacherReason(tuple.get(5, String.class));
            ticket.setObjectId(tuple.get(6, String.class));
            ticket.setStatus(TicketConstant.Status.valueOf(status));
            ticket.setAdditionalInfo(this.additionalInfo(ticket.getObjectId(), ticket.getTicketType()));
            return ticket;
        }).apply(this.entityManager);
    }

    public AdditionalInfo additionalInfo(String objectId, TicketConstant.Type type) {
        return this.ticketHandlerFactory.getInstance(type).getAdditionalInfo(objectId);
    }
}
