package com.kpmg.cacm.api.service;

import java.util.List;
import java.util.Map;

import com.kpmg.cacm.api.dto.constant.IncidentHistoryRecordType;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.model.IncidentHistory;

public interface IncidentHistoryService {

    void addRecord(Incident incident, IncidentHistoryRecordType recordType, Map<String, String> params);

    List<IncidentHistory> findAllByIncidentId(Long incidentId);

}
