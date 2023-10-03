package com.kpmg.cacm.api.service;

import java.io.FileNotFoundException;

import org.springframework.core.io.Resource;

public interface AttachmentService {

    Resource getIncidentResolutionAttachment(Long incidentResolutionId) throws FileNotFoundException;

}
