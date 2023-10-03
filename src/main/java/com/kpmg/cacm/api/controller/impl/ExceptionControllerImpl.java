package com.kpmg.cacm.api.controller.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.kpmg.cacm.api.controller.ExceptionController;
import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.dto.response.ExceptionResponse;
import com.kpmg.cacm.api.dto.response.model.ExceptionDTO;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Exception;
import com.kpmg.cacm.api.service.ExceptionService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Component;

@Component
public class ExceptionControllerImpl implements ExceptionController {

    private final ExceptionService exceptionService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public ExceptionControllerImpl(
            final ExceptionService exceptionService,
            final UserService userService,
            final ModelMapper modelMapper) {
        this.exceptionService = exceptionService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExceptionResponse findAll(final Long ownerId, final String categoryName, final String exceptionStatus) {
        final List<ExceptionStatus> statuses = getExceptionStatuses(exceptionStatus);
        List<Category> categories = getAuthorizedCategories(categoryName);
        final List<ExceptionDTO> exceptions = this.exceptionService.findAllByCategoriesAndOwnerAndStatuses(categories, ownerId, statuses)
            .stream()
            .map(user -> this.modelMapper.map(user, ExceptionDTO.class))
            .collect(Collectors.toList());
        return ExceptionResponse.builder().exceptions(exceptions).build();
    }

    private List<Category> getAuthorizedCategories(String categoryName) {
        List<Category> categories = null;
        if (categoryName != null) {
            for (final Category category : this.userService.getCurrentUser().getCategories()) {
                if(category.getName().equals(categoryName)) {
                    categories = Collections.singletonList(category);
                    break;
                }
            }
        } else {
            categories = this.userService.getCurrentUser().getCategories();
        }
        if (categories == null) {
            throw new NotAuthorizedException("Not authorized to access any specified categories");
        }
        return categories;
    }

    @Override
    public ExceptionResponse findById(Long id) {
        final Exception exception = this.exceptionService.findById(id);
        if (!this.userService.getCurrentUser().getCategories().contains(
                exception.getExceptionDefinitionRun().getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        return ExceptionResponse.builder().exceptions(
            Collections.singletonList(this.modelMapper.map(exception, ExceptionDTO.class))
        ).build();
    }

    private List<ExceptionStatus> getExceptionStatuses(String exceptionStatus) {
        List<ExceptionStatus> statuses = null;
        if (exceptionStatus != null) {
            statuses = Collections.singletonList(ExceptionStatus.valueOf(exceptionStatus.toUpperCase()));
        }
        return statuses;
    }

    @Override
    public DataTablesOutput<ExceptionDTO> findAllDt(@Valid DataTablesInput dataTablesInput, Long ownerId, String categoryName, final String exceptionStatus) {
        final List<ExceptionStatus> statuses = getExceptionStatuses(exceptionStatus);
        List<Category> categories = getAuthorizedCategories(categoryName);
        return this.exceptionService.findAllByCategoriesAndOwnerDt(
            categories,
            ownerId,
            statuses,
            dataTablesInput,
            exception-> this.modelMapper.map(exception, ExceptionDTO.class)
        );
    }
}
