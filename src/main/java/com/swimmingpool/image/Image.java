package com.swimmingpool.image;

import com.swimmingpool.common.entity.AuditTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "_image")
public class Image extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Lob
    @Column(name = "url")
    private String url;

    @Column(name = "object_id")
    private String objectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ImageConstant.Type type;
}
