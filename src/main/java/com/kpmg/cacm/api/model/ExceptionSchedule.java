package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.ScheduleFrequency;
import com.kpmg.cacm.api.dto.constant.ScheduleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionSchedule extends AbstractBaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;


    @Enumerated(EnumType.STRING)
    private ScheduleFrequency scheduleFrequency;

    private Long exceptionDefinitionId;
}
