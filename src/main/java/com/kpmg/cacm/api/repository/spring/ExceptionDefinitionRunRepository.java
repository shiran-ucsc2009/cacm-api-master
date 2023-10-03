package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionDefinitionRunRepository extends JpaRepository<ExceptionDefinitionRun, Long> {

    ExceptionDefinitionRun findByIdAndDeletedFalse(Long id);

    List<ExceptionDefinitionRun> findAllByDeletedFalse();

    long countAllByDeletedFalse();

}
