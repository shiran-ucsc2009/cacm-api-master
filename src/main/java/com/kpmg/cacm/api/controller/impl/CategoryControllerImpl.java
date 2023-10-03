package com.kpmg.cacm.api.controller.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.CategoryController;
import com.kpmg.cacm.api.dto.request.CategoryAddRequest;
import com.kpmg.cacm.api.dto.request.CategoryUpdateRequest;
import com.kpmg.cacm.api.dto.response.CategoryResponse;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.model.CategoryDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.service.CategoryService;
import com.kpmg.cacm.api.service.UserService;
import com.kpmg.cacm.api.validator.CategoryAddRequestValidator;
import com.kpmg.cacm.api.validator.CategoryUpdateRequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Component
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final CategoryAddRequestValidator categoryAddRequestValidator;

    private final CategoryUpdateRequestValidator categoryUpdateRequestValidator;

    @Autowired
    public CategoryControllerImpl(
            final CategoryService categoryService,
            final UserService userService,
            final ModelMapper modelMapper,
            final CategoryAddRequestValidator categoryAddRequestValidator,
            final CategoryUpdateRequestValidator categoryUpdateRequestValidator) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.categoryAddRequestValidator = categoryAddRequestValidator;
        this.categoryUpdateRequestValidator = categoryUpdateRequestValidator;
    }

    @InitBinder("CategoryAddRequest")
    protected final void setupAddBinder(final WebDataBinder binder) {
        binder.addValidators(this.categoryAddRequestValidator);
    }

    @InitBinder("CategoryUpdateRequest")
    protected final void setupUpdateBinder(final WebDataBinder binder) {
        binder.addValidators(this.categoryUpdateRequestValidator);
    }

    @Override
    public EmptyResponse save(final CategoryAddRequest categoryAddRequest) {
        this.categoryService.save(this.modelMapper.map(categoryAddRequest, Category.class));
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse update(final CategoryUpdateRequest categoryUpdateRequest) {
        final Category category = this.categoryService.findById(categoryUpdateRequest.getId());
        this.modelMapper.map(categoryUpdateRequest, category);
        this.categoryService.save(category);
        return EmptyResponse.builder().build();
    }

    @Override
    public CategoryResponse findById(final Long id) {
        return CategoryResponse.builder().categories(
            Collections.singletonList(this.modelMapper.map(
                this.categoryService.findById(id),
                CategoryDTO.class
            )
        )).build();
    }

    @Override
    public CategoryResponse findAll() {
        final List<CategoryDTO> categories = this.categoryService.findAll()
            .stream()
            .filter(category -> this.userService.getCurrentUser().getCategories().contains(category))
            .map(category -> this.modelMapper.map(category, CategoryDTO.class))
            .collect(Collectors.toList());
        return CategoryResponse.builder().categories(categories).build();
    }

    @Override
    public EmptyResponse deleteById(final Long id) {
        this.categoryService.deleteById(id);
        return EmptyResponse.builder().build();
    }

    @Override
    public CountResponse countAll() {
        return CountResponse.builder().count(this.categoryService.countAll()).build();
    }

}
