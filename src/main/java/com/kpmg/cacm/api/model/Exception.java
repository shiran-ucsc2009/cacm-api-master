package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Exception extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    private ExceptionDefinitionRun exceptionDefinitionRun;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExceptionStatus status;

    private int incidentCount;
}
