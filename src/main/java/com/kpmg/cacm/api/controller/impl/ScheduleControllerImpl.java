package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.ScheduleController;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.schedule.spring.ExpDefRunSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleControllerImpl implements ScheduleController {

    private final ExpDefRunSchedule expDefRunSchedule;

    @Autowired
    public ScheduleControllerImpl(final ExpDefRunSchedule expDefRunSchedule) {
        this.expDefRunSchedule = expDefRunSchedule;
    }

    @Override
    public EmptyResponse runAutomaticExceptionDefs() {
        this.expDefRunSchedule.runExceptionDef();
        return EmptyResponse.builder().build();
    }
}
