package com.kpmg.cacm.api.validator;

import com.kpmg.cacm.api.dto.request.CategoryAddRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryAddRequestValidator implements Validator {

    @Override
    public final boolean supports(final Class<?> clazz) {
        return CategoryAddRequest.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {

    }

}
