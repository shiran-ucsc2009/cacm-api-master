package com.kpmg.cacm.api.controller.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.kpmg.cacm.api.controller.PrivilegeMappingController;
import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.dto.request.PrivilegeMappingRequest;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.PrivilegeMappingResponse;
import com.kpmg.cacm.api.dto.response.model.PrivilegeMappingDTO;
import com.kpmg.cacm.api.service.PrivilegeMappingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeMappingControllerImpl implements PrivilegeMappingController {

    private final PrivilegeMappingService privilegeMappingService;

    private final ModelMapper modelMapper;

    @Autowired
    public PrivilegeMappingControllerImpl(
            final ModelMapper modelMapper,
            final PrivilegeMappingService privilegeMappingService) {
        this.modelMapper = modelMapper;
        this.privilegeMappingService = privilegeMappingService;
    }

    @Override
    public PrivilegeMappingResponse findAll(final Long groupId, final Long privilegeId, final PrivilegeType privilegeType) {
        final List<PrivilegeMappingDTO> privilegeMappings = this.privilegeMappingService.findAll(groupId, privilegeId, privilegeType)
            .stream()
            .map(privilegeMapping -> this.modelMapper.map(privilegeMapping, PrivilegeMappingDTO.class))
            .collect(Collectors.toList());
        return PrivilegeMappingResponse.builder().privilegeMappings(privilegeMappings).build();
    }

    @Override
    public PrivilegeMappingResponse findPrivilegeMappingsForCurrentUser(PrivilegeType privilegeType) {
        final List<PrivilegeMappingDTO> privilegeMappings = this.privilegeMappingService.findPrivilegeMappingsForCurrentUser(privilegeType)
            .stream()
            .map(privilegeMapping -> this.modelMapper.map(privilegeMapping, PrivilegeMappingDTO.class))
            .collect(Collectors.toList());
        return PrivilegeMappingResponse.builder().privilegeMappings(privilegeMappings).build();
    }

    @Override
    public EmptyResponse save(@Valid final PrivilegeMappingRequest privilegeMappingRequest) {
        this.privilegeMappingService.save(privilegeMappingRequest.getUserGroupId(), privilegeMappingRequest.getPrivileges());
        return EmptyResponse.builder().build();
    }

    @Override
    public PrivilegeMappingResponse findById(final Long id) {
        return PrivilegeMappingResponse.builder().privilegeMappings(
            Collections.singletonList(
                this.modelMapper.map(this.privilegeMappingService.findById(id), PrivilegeMappingDTO.class)
            )
        ).build();
    }
}
