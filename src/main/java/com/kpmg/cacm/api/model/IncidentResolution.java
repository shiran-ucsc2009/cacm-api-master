package com.kpmg.cacm.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentResolution extends AbstractBaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment attachment;

    @ManyToOne
    @NotNull
    private IncidentCause incidentCause;

    @OneToOne(cascade = CascadeType.ALL)
    private IncidentFeedback incidentFeedback;

    private Long incidentId;

}
