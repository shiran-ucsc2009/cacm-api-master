package com.kpmg.cacm.api.controller.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.ExceptionDefinitionController;
import com.kpmg.cacm.api.dto.request.ExceptionDefinitionAddRequest;
import com.kpmg.cacm.api.dto.request.ExceptionDefinitionUpdateRequest;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.ExceptionDefinitionResponse;
import com.kpmg.cacm.api.dto.response.model.ExceptionDefinitionDTO;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.model.AbstractBaseEntity;
import com.kpmg.cacm.api.model.ExceptionDefinition;
import com.kpmg.cacm.api.service.ExceptionDefinitionService;
import com.kpmg.cacm.api.service.UserService;
import com.kpmg.cacm.api.validator.ExceptionDefinitionAddRequestValidator;
import com.kpmg.cacm.api.validator.ExceptionDefinitionUpdateRequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Component
public class ExceptionDefinitionControllerImpl implements ExceptionDefinitionController {

    private final ExceptionDefinitionService exceptiondefinitionService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final ExceptionDefinitionAddRequestValidator exceptionDefAddReqValidator;

    private final ExceptionDefinitionUpdateRequestValidator exceptionDefUpdateReqValidator;

    @Autowired
    public ExceptionDefinitionControllerImpl(
            final ExceptionDefinitionService exceptiondefinitionService,
            final UserService userService,
            final ModelMapper modelMapper,
            final ExceptionDefinitionAddRequestValidator exceptionDefAddReqValidator,
            final ExceptionDefinitionUpdateRequestValidator exceptionDefUpdateReqValidator) {
        this.exceptiondefinitionService = exceptiondefinitionService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.exceptionDefAddReqValidator = exceptionDefAddReqValidator;
        this.exceptionDefUpdateReqValidator = exceptionDefUpdateReqValidator;
    }

    @InitBinder("ExceptionDefinitionAddRequest")
    protected final void setupAddBinder(final WebDataBinder binder) {
        binder.addValidators(this.exceptionDefAddReqValidator);
    }

    @InitBinder("ExceptionDefinitionUpdateRequest")
    protected final void setupUpdateBinder(final WebDataBinder binder) {
        binder.addValidators(this.exceptionDefUpdateReqValidator);
    }

    @Override
    public EmptyResponse save(final ExceptionDefinitionAddRequest exceptionDefAddRequest) {
        if (!this.userService.getCurrentUser().getCategories()
                .stream()
                .map(AbstractBaseEntity::getId)
                .collect(Collectors.toList())
                .contains(exceptionDefAddRequest.getCategoryId())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.exceptiondefinitionService.save(this.modelMapper.map(exceptionDefAddRequest, ExceptionDefinition.class));
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse update(final ExceptionDefinitionUpdateRequest exceptionDefUpdateRequest) {
        final ExceptionDefinition exceptionDef = this.exceptiondefinitionService.findById(exceptionDefUpdateRequest.getId());
        if (!this.userService.getCurrentUser().getCategories().contains(exceptionDef.getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.modelMapper.map(exceptionDefUpdateRequest, exceptionDef);
        this.exceptiondefinitionService.save(exceptionDef);
        return EmptyResponse.builder().build();
    }

    @Override
    public ExceptionDefinitionResponse findById(final Long id) {
        final ExceptionDefinition exceptionDef = this.exceptiondefinitionService.findById(id);
        if (!this.userService.getCurrentUser().getCategories().contains(exceptionDef.getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        return ExceptionDefinitionResponse.builder().exceptionDefinitions(
            Collections.singletonList(this.modelMapper.map(exceptionDef, ExceptionDefinitionDTO.class)
        )).build();
    }

    @Override
    public ExceptionDefinitionResponse findAll(final Long ownerId) {
        final List<ExceptionDefinitionDTO> exceptionDefinitions = this.exceptiondefinitionService
            .findAllByCategories(this.userService.getCurrentUser().getCategories(), ownerId)
            .stream()
            .map(exceptionDefinition -> this.modelMapper.map(exceptionDefinition, ExceptionDefinitionDTO.class))
            .collect(Collectors.toList());
        return ExceptionDefinitionResponse.builder().exceptionDefinitions(exceptionDefinitions).build();
    }

    @Override
    public EmptyResponse deleteById(final Long id) {
        final ExceptionDefinition exceptionDef = this.exceptiondefinitionService.findById(id);
        if (!this.userService.getCurrentUser().getCategories().contains(exceptionDef.getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.exceptiondefinitionService.deleteById(id);
        return EmptyResponse.builder().build();
    }

    @Override
    public CountResponse countAll() {
        return CountResponse.builder().count(this.exceptiondefinitionService.countAllByCategories(
            this.userService.getCurrentUser().getCategories()
        )).build();
    }

}
