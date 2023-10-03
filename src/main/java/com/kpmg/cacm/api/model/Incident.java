package com.kpmg.cacm.api.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Incident extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    private Exception exception;

    @ManyToOne
    private User assignee;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String queryDataJson;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String subQueryDataJson;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentStatus status;

    private Date dueDate;

    @OneToOne(cascade = CascadeType.ALL)
    private IncidentResolution incidentResolution;

    @ManyToOne
    private Category category;

    @ManyToOne
    @NotNull
    private ClientCustomer clientCustomer;

}
