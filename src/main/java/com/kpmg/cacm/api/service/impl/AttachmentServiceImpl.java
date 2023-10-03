package com.kpmg.cacm.api.service.impl;

import java.io.FileNotFoundException;

import com.kpmg.cacm.api.dto.constant.AttachmentType;
import com.kpmg.cacm.api.repository.spring.AttachmentRepository;
import com.kpmg.cacm.api.service.AttachmentService;
import com.kpmg.cacm.api.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final FileStorageService fileStorageService;

    @Value("${cacm.config.incident-resolution.attachment-path}")
    private String resolutionAttachmentPath;

    @Autowired
    public AttachmentServiceImpl(
            final AttachmentRepository attachmentRepository,
            final FileStorageService fileStorageService) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Resource getIncidentResolutionAttachment(final Long incidentResolutionId) throws FileNotFoundException {
        return this.fileStorageService.loadFileAsResource(this.resolutionAttachmentPath +
            this.attachmentRepository.findByEntityIdAndAttachmentTypeAndDeletedFalse(
                incidentResolutionId,
                AttachmentType.INCIDENT_RESOLUTION
            ).getName()
        );
    }

}
