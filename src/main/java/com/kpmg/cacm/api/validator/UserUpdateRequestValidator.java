package com.kpmg.cacm.api.validator;

import com.kpmg.cacm.api.dto.request.UserUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserUpdateRequestValidator implements Validator {

    @Override
    public final boolean supports(final Class<?> clazz) {
        return UserUpdateRequest.class.equals(clazz);
    }

    @Override
    public final void validate(final Object target, final Errors errors) {
        final UserUpdateRequest userUpdateRequest = (UserUpdateRequest) target;

        ValidationUtils.rejectIfEmpty(errors, "userGroupId", "NotEmpty", "cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "cannot be empty");
        if (CollectionUtils.isEmpty(userUpdateRequest.getCategoryIds())) {
            errors.rejectValue("categoryIds", "NotEmpty", "cannot be empty");
        }
    }
}
