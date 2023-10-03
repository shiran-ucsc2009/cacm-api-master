package com.kpmg.cacm.api.util.email.template;

import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import lombok.Builder;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

@Builder
public class ErrorNotificationScheduleEmailTemplate implements EmailTemplate {

    private static final String TEMPLATE_NAME = "email/error_notification_schedule_email_template";

    private List<ExceptionDefinitionRunError> expDefRunErrors;

    @Override
    public final String getTemplateName() {
        return ErrorNotificationScheduleEmailTemplate.TEMPLATE_NAME;
    }

    @Override
    public IContext getContext() {
        final Context context = new Context();
        context.setVariable("expDefRunErrors", expDefRunErrors);
        return context;
    }
}
