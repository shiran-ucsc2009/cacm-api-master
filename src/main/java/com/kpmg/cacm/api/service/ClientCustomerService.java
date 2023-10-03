package com.kpmg.cacm.api.service;

import java.util.List;
import java.util.function.Function;

import com.kpmg.cacm.api.dto.response.model.ClientCustomerDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ClientCustomer;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ClientCustomerService {

    ClientCustomer findByCustomerId(String customerId);

    ClientCustomer findById(Long Id);

    void save(ClientCustomer clientCustomer);

    ClientCustomer saveIfNotExists(String clientCustomerId, String clientCustomerName,Long priority);

    ClientCustomer flagUnFlagCustomer(Long id,boolean customerFlag);

    DataTablesOutput<ClientCustomerDTO> getClientCustomerWiseIncidentCountDt(List<Category> categories, DataTablesInput dataTablesInput, Function<ClientCustomer, ClientCustomerDTO> converter);

}
