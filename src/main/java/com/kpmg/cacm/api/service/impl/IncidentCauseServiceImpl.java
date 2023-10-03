package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.IncidentCause;
import com.kpmg.cacm.api.repository.spring.IncidentCauseRepository;
import com.kpmg.cacm.api.service.IncidentCauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentCauseServiceImpl implements IncidentCauseService {

    private final IncidentCauseRepository incidentCauseRepository;

    @Autowired
    public IncidentCauseServiceImpl(final IncidentCauseRepository incidentCauseRepository) {
        this.incidentCauseRepository = incidentCauseRepository;
    }

    @Override
    public IncidentCause save(final IncidentCause incidentCause) {
        return this.incidentCauseRepository.save(incidentCause);
    }

    @Override
    public IncidentCause findById(final Long id) {
        return this.incidentCauseRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<IncidentCause> findAll() {
        return this.incidentCauseRepository.findAllByDeletedFalse();
    }

    @Override
    public void deleteById(final Long id) {
        final IncidentCause incidentCause = this.findById(id);
        incidentCause.setDeleted(true);
        incidentCause.setDeletionToken(new Date().getTime());
        this.save(incidentCause);
    }

    @Override
    public long countAll() {
        return this.incidentCauseRepository.countAllByDeletedFalse();
    }

}
