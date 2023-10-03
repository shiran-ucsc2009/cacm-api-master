package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.IncidentHistoryRecordType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentHistory extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    private Incident incident;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentHistoryRecordType recordType;

    @NotNull
    private String historyRecord;
}
