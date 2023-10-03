package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.request.IncidentCommentAddRequest;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.IncidentCommentResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IncidentCommentController {

    @Secured("FIND_INCIDENT_COMMENTS_BY_INCIDENT")
    @GetMapping("/exceptions/incidents/comments")
    IncidentCommentResponse findByIncident(@RequestParam("incident") Long incidentId);

    @Secured("ADD_INCIDENT_COMMENT")
    @PostMapping("/exceptions/incidents/comments")
    EmptyResponse save(@Valid @RequestBody IncidentCommentAddRequest incidentCommentAddRequest);

}
