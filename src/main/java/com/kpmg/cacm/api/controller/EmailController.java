package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.EmptyResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface EmailController {

    @Secured("SEND_TEST_EMAIL")
    @GetMapping("/send-email")
    EmptyResponse sendTestEmail();

    @Secured("SEND_INCIDENT_EMAIL")
    @GetMapping("/send-incident-email")
    EmptyResponse sendIncidentEmail();
}
