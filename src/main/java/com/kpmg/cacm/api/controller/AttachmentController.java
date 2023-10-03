package com.kpmg.cacm.api.controller;

import java.io.FileNotFoundException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface AttachmentController {

    @Secured("DOWNLOAD_INCIDENT_RESOLUTION_ATTACHMENT")
    @GetMapping("/incident-resolution/attachment/{resolution}")
    ResponseEntity<Resource> downloadIncidentResolutionAttachment(@PathVariable("resolution") Long incidentResolutionId) throws FileNotFoundException;
}
