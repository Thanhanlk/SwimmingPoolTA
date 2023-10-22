package com.swimmingpool.ticket;

import com.swimmingpool.common.entity.AuditTable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "_ticket")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private TicketConstant.Type ticketType;

    @Column(name = "teacher_reason")
    private String teacherReason;

    @Column(name = "admin_reason")
    private String adminReason;

    @Column(name = "object_id")
    private String objectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketConstant.Status status;

    @Column(name = "description")
    private String description;
}
