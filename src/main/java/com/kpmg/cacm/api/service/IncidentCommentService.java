package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.IncidentComment;

public interface IncidentCommentService {

    IncidentComment findById(Long id);

    void add(IncidentComment incidentComment);

    void deleteById(Long id);

    List<IncidentComment> findAllByIncidentId(Long incidentId);

}
