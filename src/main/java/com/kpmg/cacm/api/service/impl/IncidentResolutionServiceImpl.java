package com.kpmg.cacm.api.service.impl;

import com.kpmg.cacm.api.model.IncidentResolution;
import com.kpmg.cacm.api.repository.spring.IncidentResolutionRepository;
import com.kpmg.cacm.api.service.IncidentResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentResolutionServiceImpl implements IncidentResolutionService {

    private final IncidentResolutionRepository incidentResolutionRepository;

    @Autowired
    public IncidentResolutionServiceImpl(IncidentResolutionRepository incidentResolutionRepository) {
        this.incidentResolutionRepository = incidentResolutionRepository;
    }

    @Override
    public IncidentResolution findByIncidentId(Long id) {
        return this.incidentResolutionRepository.findByIncidentId(id);
    }
}
