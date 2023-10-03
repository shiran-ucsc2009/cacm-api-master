package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.IncidentCause;

public interface IncidentCauseService {

    IncidentCause save(IncidentCause incidentCause);

    IncidentCause findById(Long id);

    List<IncidentCause> findAll();

    void deleteById(Long id);

    long countAll();

}
