package com.kpmg.cacm.api.controller.impl;

import java.util.Collections;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.UserGroupController;
import com.kpmg.cacm.api.dto.request.UserGroupAddRequest;
import com.kpmg.cacm.api.dto.request.UserGroupUpdateRequest;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.UserGroupResponse;
import com.kpmg.cacm.api.dto.response.model.UserGroupDTO;
import com.kpmg.cacm.api.model.UserGroup;
import com.kpmg.cacm.api.service.UserGroupService;
import com.kpmg.cacm.api.validator.UserGroupAddRequestValidator;
import com.kpmg.cacm.api.validator.UserGroupUpdateRequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Component
public class UserGroupControllerImpl implements UserGroupController {

    private final UserGroupService userGroupService;

    private final ModelMapper modelMapper;

    private final UserGroupAddRequestValidator userGroupAddRequestValidator;

    private final UserGroupUpdateRequestValidator userGroupUpdateRequestValidator;

    @Autowired
    public UserGroupControllerImpl(final UserGroupService userGroupService, final ModelMapper modelMapper, final UserGroupAddRequestValidator userGroupAddRequestValidator, final UserGroupUpdateRequestValidator userGroupUpdateRequestValidator) {
        this.userGroupService = userGroupService;
        this.modelMapper = modelMapper;
        this.userGroupAddRequestValidator = userGroupAddRequestValidator;
        this.userGroupUpdateRequestValidator = userGroupUpdateRequestValidator;
    }

    @InitBinder("UserGroupAddRequest")
    protected final void setupAddBinder(final WebDataBinder binder) {
        binder.addValidators(this.userGroupAddRequestValidator);
    }

    @InitBinder("UserGroupUpdateRequest")
    protected final void setupUpdateBinder(final WebDataBinder binder) {
        binder.addValidators(this.userGroupUpdateRequestValidator);
    }

    @Override
    public UserGroupResponse findById(final Long id) {
        return UserGroupResponse.builder().userGroups(
            Collections.singletonList(this.modelMapper.map(
                this.userGroupService.findById(id),
                UserGroupDTO.class)
            )
        ).build();
    }

    @Override
    public UserGroupResponse findAll() {
        return UserGroupResponse.builder().userGroups(
            this.userGroupService.findAll()
                .stream()
                .map(userGroup -> this.modelMapper.map(userGroup, UserGroupDTO.class))
                .collect(Collectors.toList())
        ).build();
    }

    @Override
    public EmptyResponse deleteById(final Long id) {
        this.userGroupService.deleteById(id);
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse save(final UserGroupAddRequest userGroupAddRequest) {
        this.userGroupService.save(this.modelMapper.map(userGroupAddRequest, UserGroup.class));
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse update(final UserGroupUpdateRequest userGroupUpdateRequest) {
        final UserGroup userGroup = this.userGroupService.findById(userGroupUpdateRequest.getId());
        this.modelMapper.map(userGroupUpdateRequest, userGroup);
        this.userGroupService.update(userGroup);
        return EmptyResponse.builder().build();
    }

    @Override
    public CountResponse countAll() {
        return CountResponse.builder().count(this.userGroupService.countAll()).build();
    }

}
