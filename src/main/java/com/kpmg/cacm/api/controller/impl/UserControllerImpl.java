package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.UserController;
import com.kpmg.cacm.api.dto.request.PasswordFirstAttemptRequest;
import com.kpmg.cacm.api.dto.request.PasswordUpdateRequest;
import com.kpmg.cacm.api.dto.request.UserAddRequest;
import com.kpmg.cacm.api.dto.request.UserUpdateRequest;
import com.kpmg.cacm.api.dto.response.CommonMessageResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.PasswordUpdateResponse;
import com.kpmg.cacm.api.dto.response.UserResponse;
import com.kpmg.cacm.api.dto.response.model.UserDTO;
import com.kpmg.cacm.api.exceptions.PreConditionFailedException;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.service.UserService;
import com.kpmg.cacm.api.validator.UserUpdateRequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserControllerImpl implements UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final UserUpdateRequestValidator userAddRequestValidator;

    private final UserUpdateRequestValidator userUpdateRequestValidator;

    @Value("${cacm.config.user.expiry.grace.peroid}")
    protected long PasswordExpireGracePeriod ;

    @Autowired
    public UserControllerImpl(
            final UserService userService,
            final ModelMapper modelMapper,
            final UserUpdateRequestValidator userAddRequestValidator,
            final UserUpdateRequestValidator userUpdateRequestValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userAddRequestValidator = userAddRequestValidator;
        this.userUpdateRequestValidator = userUpdateRequestValidator;
    }

    @InitBinder("UserAddRequest")
    protected final void setupAddBinder(final WebDataBinder binder) {
        binder.addValidators(this.userAddRequestValidator);
    }

    @InitBinder("UserUpdateRequest")
    protected final void setupUpdateBinder(final WebDataBinder binder) {
        binder.addValidators(this.userUpdateRequestValidator);
    }

    @Override
    public UserResponse findAll(final String groupName, final Long categoryId) {
        final List<UserDTO> users = this.userService.findAll(groupName, categoryId)
                .stream()
                .map(user -> this.modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return UserResponse.builder().users(users).build();
    }

    @Override
    public UserResponse findById(Long id) {
        return UserResponse.builder().users(
            Collections.singletonList(
                this.modelMapper.map(this.userService.findById(id), UserDTO.class)
            )
        ).build();
    }

    @Override
    public Resource findUserImage(final Long id) throws FileNotFoundException {
        return this.userService.getUserProfileImage(id);
    }

    @Override
    public UserResponse findCurrent() {
        return UserResponse.builder().users(
            Collections.singletonList(
                this.modelMapper.map(this.userService.getCurrentUser(), UserDTO.class)
            )
        ).build();
    }

    @Override
    public EmptyResponse save(MultipartFile file, final UserAddRequest userAddRequest) {
        this.userService.save(file,this.modelMapper.map(userAddRequest, User.class));
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse update(MultipartFile file, final UserUpdateRequest userUpdateRequest) {
        final User user = this.userService.findById(userUpdateRequest.getId());
        this.modelMapper.map(userUpdateRequest, user);
        this.userService.update(file,user);
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse delete(final Long userId) {
        this.userService.deleteById(userId);
        return EmptyResponse.builder().build();
    }

    @Override
    public PasswordUpdateResponse resetPassword(@Valid PasswordUpdateRequest passwordUpdateRequest) {
        return  PasswordUpdateResponse.builder().passwordMatch(
            this.userService.resetPassword( // TODO: validate userId against current user since he should not be able to reset other users' password
                passwordUpdateRequest.getId(), // TODO : change id-->userId for better readability
                passwordUpdateRequest.getNewPassword(),
                passwordUpdateRequest.getOldPassword()
            )
        ).build();
    }

    @Override
    public PasswordUpdateResponse resetPasswordAdmin(@Valid PasswordUpdateRequest passwordUpdateRequest) {
        return  PasswordUpdateResponse.builder().passwordMatch(
            this.userService.resetPassword(
                passwordUpdateRequest.getId(),
                passwordUpdateRequest.getNewPassword(),
                "" //TODO: Admin should be able to reset password without old password
            )
        ).build();
    }

    @Override
    public EmptyResponse resetPasswordFirstAttempt(PasswordFirstAttemptRequest passwordFirstAttemptRequest) {
        this.userService.resetPasswordFirstAttempt(passwordFirstAttemptRequest.getUsername(),passwordFirstAttemptRequest.getPassword());
        return EmptyResponse.builder().build();

    }

    @Override
    public CommonMessageResponse checkPasswordExpiration() {
        User user=this.userService.getCurrentUser();
        long numOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(),user.getExpiryDate());
        if(numOfDaysBetween<PasswordExpireGracePeriod){
            return CommonMessageResponse.builder().message("Your Password will be expired in "+numOfDaysBetween+" days.").build();

        }
        throw new PreConditionFailedException("Your Password will be expired in "+numOfDaysBetween+" days.");
    }

}
