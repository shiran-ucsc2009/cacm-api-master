package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.dto.response.model.IncidentDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.model.EmailProperty;
import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;

public interface EmailService {

    boolean sendErrorNotificationScheduleEmail(List<ExceptionDefinitionRunError> expDefRunErrors, EmailProperty emailProperty);

    boolean sendIncidentNotificationScheduleEmail(List<IncidentEmailDTO> incidentEmailDTOS);
}
