package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.dto.request.PrivilegeMappingRequest;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.PrivilegeMappingResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PrivilegeMappingController {

    @Secured("FIND_ALL_PRIVILEGE_MAPPINGS")
    @GetMapping("/privilege-mappings")
    PrivilegeMappingResponse findAll(
        @RequestParam(value = "group", required = false) Long groupId,
        @RequestParam(value = "privilege", required = false) Long privilegeId,
        @RequestParam(value = "type", required = false) PrivilegeType privilegeType
    );

    @Secured("FIND_CURRENT_USER_PRIVILEGE_MAPPINGS")
    @GetMapping("/my-privilege-mappings")
    PrivilegeMappingResponse findPrivilegeMappingsForCurrentUser(
        @RequestParam(value = "type", required = false) PrivilegeType privilegeType
    );

    @Secured("FIND_PRIVILEGE_MAPPINGS_BY_ID")
    @GetMapping("/privilege-mappings/{id}")
    PrivilegeMappingResponse findById(@PathVariable("id") Long id);

    @Secured("ADD_USER_PRIVILEGES")
    @PostMapping("/privilege-mappings")
    EmptyResponse save(@Valid @RequestBody PrivilegeMappingRequest privilegeMappingRequest);
}
