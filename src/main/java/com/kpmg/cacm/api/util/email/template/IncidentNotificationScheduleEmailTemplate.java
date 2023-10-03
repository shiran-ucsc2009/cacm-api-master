package com.kpmg.cacm.api.util.email.template;

import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import lombok.Builder;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.List;

@Builder
public class IncidentNotificationScheduleEmailTemplate implements EmailTemplate {

    private static final String TEMPLATE_NAME = "email/incident_notification_schedule_email_template";

    private List<IncidentEmailDTO> incidentEmailDTOS ;

    private String incidentUser;

    @Override
    public final String getTemplateName() {
        return IncidentNotificationScheduleEmailTemplate.TEMPLATE_NAME;
    }

    @Override
    public IContext getContext() {
        final Context context = new Context();
        context.setVariable("incidentEmailNotification", incidentEmailDTOS);
        context.setVariable("IncidentUser",incidentUser);
        return context;
    }
}
