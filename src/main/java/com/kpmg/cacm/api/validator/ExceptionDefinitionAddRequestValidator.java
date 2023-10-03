package com.kpmg.cacm.api.validator;

import com.kpmg.cacm.api.dto.constant.ScheduleType;
import com.kpmg.cacm.api.dto.request.ExceptionDefinitionAddRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ExceptionDefinitionAddRequestValidator implements Validator {

    @Override
    public final boolean supports(final Class<?> clazz) {
        return ExceptionDefinitionAddRequest.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {

        final ExceptionDefinitionAddRequest exceptionDefAddRequest=(ExceptionDefinitionAddRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "query", "NotEmpty", "cannot be empty");
        if(ScheduleType.AUTOMATIC.equals(exceptionDefAddRequest.getExceptionSchedule().getScheduleType())){
            if(exceptionDefAddRequest.getOwnerId()==null){
                errors.rejectValue("ownerId", "NotEmpty", "cannot be empty");
            } else if (exceptionDefAddRequest.getDefaultAssigneeId()==null) {
                errors.rejectValue("defaultAssigneeId", "NotEmpty", "cannot be empty");
            }else if(exceptionDefAddRequest.getExceptionSchedule().getScheduleFrequency()==null){
                errors.rejectValue("scheduleFrequency", "NotEmpty", "cannot be empty");
            }

        }

    }
}
