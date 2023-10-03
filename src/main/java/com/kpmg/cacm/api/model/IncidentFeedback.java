package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.dto.constant.IncidentFeedbackType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentFeedback extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentFeedbackType feedbackType;

    @Enumerated(EnumType.STRING)
    private IncidentApproveAction approveAction;

    @ManyToOne
    private IncidentReasons incidentReasons;

    private boolean finalized;

}
