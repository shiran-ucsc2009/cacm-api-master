package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.IncidentHistoryResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IncidentHistoryController {

    @Secured("FIND_INCIDENT_HISTORY_BY_INCIDENT_ID")
    @GetMapping("/exceptions/incidents/history")
    IncidentHistoryResponse findByIncident(@RequestParam("incident") Long incidentId);

}
