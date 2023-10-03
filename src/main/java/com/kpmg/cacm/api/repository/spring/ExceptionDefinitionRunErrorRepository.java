package com.kpmg.cacm.api.repository.spring;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionDefinitionRunErrorRepository extends JpaRepository<ExceptionDefinitionRunError, Long> {

    List<ExceptionDefinitionRunError> findAllByCreationTimestampAfterAndDeletedFalse(@Param("creationTimestamp") Date creationTimestamp);

}
