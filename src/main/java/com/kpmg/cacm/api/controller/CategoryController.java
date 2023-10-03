package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.request.CategoryAddRequest;
import com.kpmg.cacm.api.dto.request.CategoryUpdateRequest;
import com.kpmg.cacm.api.dto.response.CategoryResponse;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface CategoryController {

    @Secured("ADD_CATEGORY")
    @PostMapping("/categories")
    EmptyResponse save(@Valid @RequestBody CategoryAddRequest categoryAddRequest);

    @Secured("UPDATE_CATEGORY")
    @PutMapping("/categories")
    EmptyResponse update(@Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest);

    @Secured("FIND_CATEGORY_BY_ID")
    @GetMapping("/categories/{id}")
    CategoryResponse findById(@PathVariable(value = "id") Long id);

    @Secured("FIND_ALL_CATEGORIES")
    @GetMapping("/categories")
    CategoryResponse findAll();

    @Secured("DELETE_CATEGORY")
    @DeleteMapping("/categories/{id}")
    EmptyResponse deleteById(@PathVariable(value = "id") Long id);

    @Secured("COUNT_CATEGORIES")
    @GetMapping("/categories/count")
    CountResponse countAll();
}
