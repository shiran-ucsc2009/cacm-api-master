package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.IncidentCauseResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IncidentCauseController {

    @Secured("FIND_ALL_INCIDENT_CAUSES")
    @GetMapping("/exceptions/incidents/causes")
    IncidentCauseResponse findAll();
}
