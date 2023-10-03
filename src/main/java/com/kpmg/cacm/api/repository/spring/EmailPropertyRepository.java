package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.model.EmailProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailPropertyRepository extends JpaRepository<EmailProperty, Long> {

    EmailProperty findByNameAndDeletedFalse(@Param("name") String name);
}
