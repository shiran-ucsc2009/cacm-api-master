package com.kpmg.cacm.api.validator;

import com.kpmg.cacm.api.dto.request.UserAddRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserAddRequestValidator implements Validator {

    @Override
    public final boolean supports(final Class<?> clazz) {
        return UserAddRequest.class.equals(clazz);
    }

    @Override
    public final void validate(final Object target, final Errors errors) {
        final UserAddRequest userAddRequest = (UserAddRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "userGroupId", "NotEmpty", "cannot be empty");
        if (userAddRequest.getUsername().length() < 6) {
            errors.rejectValue("username", "MinLength", "at least 6 characters required");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "cannot be empty");
        if (userAddRequest.getPassword().length() < 8) {
            errors.rejectValue("password", "MinLength", "at least 8 characters required");
        }
        if (CollectionUtils.isEmpty(userAddRequest.getCategoryIds())) {
            errors.rejectValue("categoryIds", "NotEmpty", "cannot be empty");
        }
    }
}
