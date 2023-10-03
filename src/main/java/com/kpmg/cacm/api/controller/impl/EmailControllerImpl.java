package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.EmailController;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.schedule.spring.ErrorNotificationSchedule;
import com.kpmg.cacm.api.schedule.spring.IncidentNotificationSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailControllerImpl implements EmailController {

    private final ErrorNotificationSchedule errorNotificationSchedule;

    private final IncidentNotificationSchedule incidentNotificationSchedule;

    @Autowired
    public EmailControllerImpl(final ErrorNotificationSchedule errorNotificationSchedule, IncidentNotificationSchedule incidentNotificationSchedule) {
        this.errorNotificationSchedule = errorNotificationSchedule;
        this.incidentNotificationSchedule = incidentNotificationSchedule;
    }

    @Override
    public EmptyResponse sendTestEmail() {
        this.errorNotificationSchedule.sendErrorNotificationEmail();
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse sendIncidentEmail() {
        this.incidentNotificationSchedule.sendIncidentNotificationEmail();
        return  EmptyResponse.builder().build();
    }
}
