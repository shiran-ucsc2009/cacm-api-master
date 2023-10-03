package com.kpmg.cacm.api.validator;


import com.kpmg.cacm.api.dto.request.ExceptionDefinitionUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ExceptionDefinitionUpdateRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ExceptionDefinitionUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {


    }
}
