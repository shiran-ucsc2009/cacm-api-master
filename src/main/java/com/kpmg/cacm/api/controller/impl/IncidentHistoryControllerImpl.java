package com.kpmg.cacm.api.controller.impl;

import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.IncidentHistoryController;
import com.kpmg.cacm.api.dto.response.IncidentHistoryResponse;
import com.kpmg.cacm.api.dto.response.model.IncidentHistoryDTO;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.service.IncidentHistoryService;
import com.kpmg.cacm.api.service.IncidentService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidentHistoryControllerImpl implements IncidentHistoryController {

    private final IncidentHistoryService incidentHistoryService;

    private final IncidentService incidentService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public IncidentHistoryControllerImpl(
            final IncidentHistoryService incidentHistoryService,
            final IncidentService incidentService,
            final UserService userService,
            final ModelMapper modelMapper) {
        this.incidentHistoryService = incidentHistoryService;
        this.incidentService = incidentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public IncidentHistoryResponse findByIncident(final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        return IncidentHistoryResponse.builder().incidentHistories(
            this.incidentHistoryService.findAllByIncidentId(incidentId)
                .stream()
                .map(incidentHistory -> this.modelMapper.map(incidentHistory, IncidentHistoryDTO.class))
                .collect(Collectors.toList())
        ).build();
    }
}
