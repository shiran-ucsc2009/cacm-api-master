package com.kpmg.cacm.api.controller.impl;

import java.io.FileNotFoundException;

import com.kpmg.cacm.api.controller.AttachmentController;
import com.kpmg.cacm.api.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AttachmentControllerImpl implements AttachmentController {

    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentControllerImpl(final AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @Override
    public ResponseEntity<Resource> downloadIncidentResolutionAttachment(final Long incidentResolutionId) throws FileNotFoundException {
        final Resource resource = this.attachmentService.getIncidentResolutionAttachment(incidentResolutionId);
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + '"')
            .body(resource);
    }
}
