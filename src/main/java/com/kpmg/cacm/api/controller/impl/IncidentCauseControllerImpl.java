package com.kpmg.cacm.api.controller.impl;

import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.IncidentCauseController;
import com.kpmg.cacm.api.dto.response.IncidentCauseResponse;
import com.kpmg.cacm.api.dto.response.model.IncidentCauseDTO;
import com.kpmg.cacm.api.service.IncidentCauseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidentCauseControllerImpl implements IncidentCauseController {

    private final IncidentCauseService incidentCauseService;

    private final ModelMapper modelMapper;

    @Autowired
    public IncidentCauseControllerImpl(
            final IncidentCauseService incidentCauseService,
            final ModelMapper modelMapper) {
        this.incidentCauseService = incidentCauseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public IncidentCauseResponse findAll() {
        return IncidentCauseResponse.builder().incidentCauses(this.incidentCauseService.findAll()
            .stream()
            .map(incidentCause -> this.modelMapper.map(incidentCause, IncidentCauseDTO.class))
            .collect(Collectors.toList())
        ).build();
    }
}
