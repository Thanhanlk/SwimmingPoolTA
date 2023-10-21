package com.swimmingpool.order;

import com.swimmingpool.common.entity.AuditTable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "_order")
public class Order extends AuditTable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderConstant.Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_payment")
    private OrderConstant.MethodPayment methodPayment;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "assignment_id")
    private String assignmentId;

    @Column(name = "payment_online_id")
    private String paymentOnlineID;
}
