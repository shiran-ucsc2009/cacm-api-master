package com.kpmg.cacm.api.repository.datatables;

import com.kpmg.cacm.api.model.ClientCustomer;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCustomerRepositoryDt extends DataTablesRepository<ClientCustomer, Long> {
}
