package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.model.IncidentResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentResolutionRepository extends JpaRepository<IncidentResolution, Long> {

    IncidentResolution findByIncidentId(Long id);

}
