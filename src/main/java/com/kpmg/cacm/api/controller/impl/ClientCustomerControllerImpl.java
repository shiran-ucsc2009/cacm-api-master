package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.ClientCustomerController;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.model.ClientCustomerDTO;
import com.kpmg.cacm.api.service.ClientCustomerService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Component;

@Component
public class ClientCustomerControllerImpl implements ClientCustomerController {

    private final ClientCustomerService clientCustomerService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public ClientCustomerControllerImpl(
            final ClientCustomerService clientCustomerService,
            final UserService userService,
            final ModelMapper modelMapper) {
        this.clientCustomerService = clientCustomerService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmptyResponse flagCustomer(Long id) {
        this.clientCustomerService.flagUnFlagCustomer(id,true);
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse unFlagCustomer(Long id) {
        this.clientCustomerService.flagUnFlagCustomer(id,false);
        return EmptyResponse.builder().build();
    }

    @Override
    public DataTablesOutput<ClientCustomerDTO> customerWiseIncidentCountDT(DataTablesInput dataTablesInput) {
        return this.clientCustomerService.getClientCustomerWiseIncidentCountDt(
            this.userService.getCurrentUser().getCategories(),
            dataTablesInput,
            clientCustomer -> this.modelMapper.map(clientCustomer, ClientCustomerDTO.class)
        );
    }
}
