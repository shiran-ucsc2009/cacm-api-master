package com.kpmg.cacm.api.util.email.template;

import org.thymeleaf.context.IContext;

public interface EmailTemplate {
    String getTemplateName();
    IContext getContext();
}