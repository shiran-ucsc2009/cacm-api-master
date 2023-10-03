package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.model.ClientCustomerDTO;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ClientCustomerController {

    @Secured("FLAG_CUSTOMER")
    @PutMapping("/client-customer/flag-customer")
    EmptyResponse flagCustomer(@RequestParam("id") Long id);

    @Secured("UN_FLAG_CUSTOMER")
    @PutMapping("/client-customer/un-flag-customer")
    EmptyResponse unFlagCustomer(@RequestParam("id") Long id);

    @Secured("CUSTOMER_WISE_INCIDENT_COUNT")
    @PostMapping("/client-customer/incident-count/dt")
    DataTablesOutput<ClientCustomerDTO> customerWiseIncidentCountDT(@Valid @RequestBody DataTablesInput dataTablesInput);

}
