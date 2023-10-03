package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.PrivilegeController;
import com.kpmg.cacm.api.dto.response.PrivilegeResponse;
import com.kpmg.cacm.api.dto.response.model.PrivilegeDTO;
import com.kpmg.cacm.api.service.PrivilegeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrivilegeControllerImpl implements PrivilegeController {

    private final PrivilegeService privilegeService;

    private final ModelMapper modelMapper;

    @Autowired
    public PrivilegeControllerImpl(
            final PrivilegeService privilegeService,
            final ModelMapper modelMapper) {
        this.privilegeService = privilegeService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PrivilegeResponse findAll() {
        final List<PrivilegeDTO> privileges = this.privilegeService.findAll()
                .stream()
                .map(category -> this.modelMapper.map(category, PrivilegeDTO.class))
                .collect(Collectors.toList());
        return PrivilegeResponse.builder().privileges(privileges).build();
    }
}
