package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.IncidentCause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentCauseRepository extends JpaRepository<IncidentCause, Long> {

    IncidentCause findByIdAndDeletedFalse(Long id);

    List<IncidentCause> findAllByDeletedFalse();

    long countAllByDeletedFalse();
}
