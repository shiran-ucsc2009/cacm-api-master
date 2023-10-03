package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.IncidentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentHistoryRepository  extends JpaRepository<IncidentHistory,Long> {

    List<IncidentHistory> findAllByIncident_IdAndDeletedFalseOrderByCreationTimestampDesc(Long incidentId);
}
