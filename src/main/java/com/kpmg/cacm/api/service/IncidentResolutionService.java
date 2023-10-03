package com.kpmg.cacm.api.service;

import com.kpmg.cacm.api.model.IncidentResolution;

public interface IncidentResolutionService {

    IncidentResolution findByIncidentId(Long id);
}
