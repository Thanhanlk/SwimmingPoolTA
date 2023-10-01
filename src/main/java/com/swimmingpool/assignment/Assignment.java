package com.swimmingpool.assignment;

import com.swimmingpool.common.entity.AuditTable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Time;
import java.time.DayOfWeek;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "_assignment")
@EntityListeners(AuditingEntityListener.class)
public class Assignment extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Temporal(TemporalType.TIME)
    @Column(name = "start_time")
    private Time startTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "end_time")
    private Time endTime;

    @Column(name = "pool_id")
    private String poolId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "active")
    private Boolean active;
}
