package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.request.ExceptionDefinitionAddRequest;
import com.kpmg.cacm.api.dto.request.ExceptionDefinitionUpdateRequest;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.ExceptionDefinitionResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ExceptionDefinitionController {

    @Secured("ADD_EXCEPTION_DEFINITIONS")
    @PostMapping("/exception-definitions")
    EmptyResponse save(@Valid @RequestBody ExceptionDefinitionAddRequest exceptionDefAddRequest);

    @Secured("UPDATE_EXCEPTION_DEFINITIONS")
    @PutMapping("/exception-definitions")
    EmptyResponse update(@Valid @RequestBody ExceptionDefinitionUpdateRequest exceptionDefUpdateRequest);

    @Secured("FIND_EXCEPTION_DEFINITIONS_BY_ID")
    @GetMapping("/exception-definitions/{id}")
    ExceptionDefinitionResponse findById(@PathVariable("id") Long id);

    @Secured("FIND_ALL_EXCEPTION_DEFINITIONS")
    @GetMapping("/exception-definitions")
    ExceptionDefinitionResponse findAll(@RequestParam(value = "owner", required = false) Long ownerId);

    @Secured("DELETE_EXCEPTION_DEFINITIONS")
    @DeleteMapping("/exception-definitions/{id}")
    EmptyResponse deleteById(@PathVariable(value = "id") Long id);

    @Secured("COUNT_EXCEPTION_DEFINITIONS")
    @GetMapping("/exception-definitions/count")
    CountResponse countAll();

}
