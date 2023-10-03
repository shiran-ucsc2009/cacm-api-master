package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.PrivilegeResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PrivilegeController {

    @Secured("FIND_ALL_PRIVILEGES")
    @GetMapping("/privileges")
    PrivilegeResponse findAll();
}
