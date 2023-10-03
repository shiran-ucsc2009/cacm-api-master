package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.model.ClientCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCustomerRepository extends JpaRepository<ClientCustomer, Long> {

    ClientCustomer findByCustomerIdAndDeletedFalse(String customerId);

    ClientCustomer findByIdAndDeletedFalse(Long id);

}
