package com.kpmg.cacm.api.controller.impl;

import java.util.stream.Collectors;

import com.kpmg.cacm.api.controller.IncidentCommentController;
import com.kpmg.cacm.api.dto.request.IncidentCommentAddRequest;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.IncidentCommentResponse;
import com.kpmg.cacm.api.dto.response.model.IncidentCommentDTO;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.model.IncidentComment;
import com.kpmg.cacm.api.service.IncidentCommentService;
import com.kpmg.cacm.api.service.IncidentService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidentCommentControllerImpl implements IncidentCommentController {

    private final IncidentCommentService incidentCommentService;

    private final IncidentService incidentService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public IncidentCommentControllerImpl(
            final IncidentCommentService incidentCommentService,
            final IncidentService incidentService,
            final UserService userService,
            final ModelMapper modelMapper) {
        this.incidentCommentService = incidentCommentService;
        this.incidentService = incidentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public IncidentCommentResponse findByIncident(final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories()
                .contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        return IncidentCommentResponse.builder().incidentComments(
            this.incidentCommentService.findAllByIncidentId(incidentId)
                .stream()
                .map(incidentComment -> this.modelMapper.map(incidentComment, IncidentCommentDTO.class))
                .collect(Collectors.toList())
        ).build();
    }

    @Override
    public EmptyResponse save(final IncidentCommentAddRequest incidentCommentAddRequest) {
        this.incidentCommentService.add(this.modelMapper.map(incidentCommentAddRequest, IncidentComment.class));
        return EmptyResponse.builder().build();
    }
}
