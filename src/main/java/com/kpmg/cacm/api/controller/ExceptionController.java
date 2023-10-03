package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.ExceptionResponse;
import com.kpmg.cacm.api.dto.response.model.ExceptionDTO;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public interface ExceptionController {

    @Secured("FIND_ALL_EXCEPTIONS")
    @GetMapping("/exceptions")
    ExceptionResponse findAll(
        @RequestParam(value = "owner", required = false) Long ownerId,
        @RequestParam(value = "category", required = false) String categoryName,
        @RequestParam(value = "status", required = false) String exceptionStatus
    );

    @Secured("FIND_EXCEPTION_BY_ID")
    @GetMapping("/exceptions/{id}")
    ExceptionResponse findById(@PathVariable("id") Long id);

    @Secured("FIND_ALL_EXCEPTIONS")
    @PostMapping("/exceptions/dt")
    DataTablesOutput<ExceptionDTO> findAllDt(
        @Valid @RequestBody DataTablesInput dataTablesInput,
        @RequestParam(value = "owner", required = false) Long ownerId,
        @RequestParam(value = "category", required = false) String categoryName,
        @RequestParam(value = "status", required = false) String exceptionStatus
    );

}
