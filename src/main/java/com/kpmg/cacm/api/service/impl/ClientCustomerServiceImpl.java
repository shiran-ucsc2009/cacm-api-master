package com.kpmg.cacm.api.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.dto.constant.Priority;
import com.kpmg.cacm.api.dto.response.model.ClientCustomerDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ClientCustomer;
import com.kpmg.cacm.api.repository.datatables.ClientCustomerRepositoryDt;
import com.kpmg.cacm.api.repository.spring.ClientCustomerRepository;
import com.kpmg.cacm.api.repository.spring.IncidentRepository;
import com.kpmg.cacm.api.service.ClientCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientCustomerServiceImpl implements ClientCustomerService {

    private final ClientCustomerRepository clientCustomerRepository;

    private final ClientCustomerRepositoryDt clientCustomerRepositoryDt;

    private final IncidentRepository incidentRepository;

    @Autowired
    public ClientCustomerServiceImpl(
            final ClientCustomerRepository clientCustomerRepository,
            final ClientCustomerRepositoryDt clientCustomerRepositoryDt,
            final IncidentRepository incidentRepository) {
        this.clientCustomerRepository = clientCustomerRepository;
        this.clientCustomerRepositoryDt = clientCustomerRepositoryDt;
        this.incidentRepository = incidentRepository;
    }

    @Override
    public ClientCustomer findByCustomerId(final String customerId) {
        return this.clientCustomerRepository.findByCustomerIdAndDeletedFalse(customerId);
    }

    @Override
    public ClientCustomer findById(final Long id) {
        return this.clientCustomerRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void save(final ClientCustomer clientCustomer) {
        this.clientCustomerRepository.save(clientCustomer);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ClientCustomer saveIfNotExists(final String clientCustomerId, final String clientCustomerName, final Long clientCustomerRisk) {
        ClientCustomer clientCustomer = this.findByCustomerId(clientCustomerId);

        if(clientCustomer == null){
            clientCustomer = new ClientCustomer();
            clientCustomer.setCustomerId(clientCustomerId);
            clientCustomer.setCustomerName(clientCustomerName);
            clientCustomer.setPriority(Priority.valueOf((int) (long) clientCustomerRisk));
            this.save(clientCustomer);
        }

        return clientCustomer;
    }

    @Override
    public ClientCustomer flagUnFlagCustomer(Long id, boolean customerFlag) {
        final ClientCustomer clientCustomer = this.findById(id);
        clientCustomer.setCustomerFlag(customerFlag);
        return this.clientCustomerRepository.save(clientCustomer);
    }

    @Override
    public DataTablesOutput<ClientCustomerDTO> getClientCustomerWiseIncidentCountDt(List<Category> categories, DataTablesInput dataTablesInput, Function<ClientCustomer, ClientCustomerDTO> converter) {
        dataTablesInput.setColumns(dataTablesInput.getColumns().stream().filter(column -> !"incidentCount".equals(column.getData())).collect(Collectors.toList()));
        final DataTablesOutput<ClientCustomerDTO> clientCustomerDTOs = this.clientCustomerRepositoryDt.findAll(
            dataTablesInput,
            null,
            (Specification<ClientCustomer>) (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")),
            converter
        );
        clientCustomerDTOs.getData().forEach(dtClientCustomerDTO -> {
            dtClientCustomerDTO.setIncidentCount(
                this.incidentRepository.countAllByCategoryInAndStatusAndDeletedFalse(categories, null, dtClientCustomerDTO.getCustomerId(), null, null)
            );
        });
        return clientCustomerDTOs;
    }

}
