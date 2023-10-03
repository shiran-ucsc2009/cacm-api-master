package com.kpmg.cacm.api.service.impl;

import java.util.List;
import java.util.Map;

import com.kpmg.cacm.api.dto.constant.IncidentHistoryRecordType;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.model.IncidentHistory;
import com.kpmg.cacm.api.repository.spring.IncidentHistoryRepository;
import com.kpmg.cacm.api.service.IncidentHistoryService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentHistoryServiceImpl implements IncidentHistoryService {

    private final IncidentHistoryRepository incidentHistoryRepository;

    private final UserService userService;

    @Autowired
    public IncidentHistoryServiceImpl(
            final IncidentHistoryRepository incidentHistoryRepository,
            final UserService userService) {
        this.incidentHistoryRepository = incidentHistoryRepository;
        this.userService = userService;
    }

    @Override
    public final void addRecord(final Incident incident, final IncidentHistoryRecordType recordType, final Map<String, String> params) {

        final String loggedUser = this.userService.getCurrentUser().getName();

        final StringBuilder record = new StringBuilder(16);
        switch (recordType) {
            case CREATE:
                record.append("Incident was created by ").append(loggedUser);
                break;
            case ASSIGN:
                record.append(loggedUser).append(" assigned the incident to ").append(params.get("assignee"));
                if (params.get("comment") != null && !params.get("comment").trim().isEmpty()) {
                    record.append('|').append(params.get("comment"));
                }
                if (params.get("dueDate") != null && !params.get("dueDate").trim().isEmpty()) {
                    this.addRecord(incident, IncidentHistoryRecordType.SET_DUE_DATE, params);
                }
                break;
            case SET_DUE_DATE:
                record.append(loggedUser).append(" set the due date to ").append(params.get("dueDate"));
                break;
            case COMMENT:
                record.append(loggedUser).append(" commented on incident|").append(params.get("comment"));
                break;
            case RESOLVE:
                record.append(loggedUser).append(" resolve the incident with cause \" ")
                    .append(params.get("cause").trim()).append(" \" and send for approval");
                if (params.get("comment") != null && !params.get("comment").trim().isEmpty()) {
                    record.append('|').append(params.get("comment"));
                }
                if (params.get("file") != null && !params.get("file").trim().isEmpty()) {
                   record.append('|').append(params.get("file"));
                }
                break;
            case APPROVE_RESOLUTION:
                record.append(loggedUser).append(" approved and ")
                    .append((params.get("action") + "ed").toLowerCase()).append(" the resolution");
                if(params.get("reason") != null && !params.get("reason").trim().isEmpty()){
                    record.append(" -  Reason : ").append(params.get("reason"));
                }
                if (params.get("comment") != null && !params.get("comment").trim().isEmpty()) {
                    record.append('|').append(params.get("comment"));
                }
                break;
            case OVERRIDE_APPROVAL:
                record.append(loggedUser).append(" has overridden the approval and ")
                    .append(" marked as ").append((params.get("action") + "ed").toLowerCase());
                if (params.get("comment") != null && !params.get("comment").trim().isEmpty()) {
                    record.append('|').append(params.get("comment"));
                }
                break;
            case REJECT_RESOLUTION:
                record.append(loggedUser).append(" rejected the resolution");
                this.addRecord(incident, IncidentHistoryRecordType.ASSIGN, params);
                break;
            case FINALIZE_APPROVAL:
                record.append(loggedUser).append(" finalized the resolution");
                if (params.get("comment") != null && !params.get("comment").trim().isEmpty()) {
                    record.append('|').append(params.get("comment"));
                }
                break;
            case REJECT_APPROVAL:
                record.append(loggedUser).append(" rejected the approval");
                this.addRecord(incident, IncidentHistoryRecordType.ASSIGN, params);
                break;
        }

        final IncidentHistory incidentHistory = new IncidentHistory();
        incidentHistory.setIncident(incident);
        incidentHistory.setRecordType(recordType);
        incidentHistory.setHistoryRecord(record.toString());
        this.incidentHistoryRepository.save(incidentHistory);
    }

    @Override
    public List<IncidentHistory> findAllByIncidentId(final Long incidentId) {
        return this.incidentHistoryRepository.findAllByIncident_IdAndDeletedFalseOrderByCreationTimestampDesc(incidentId);
    }

}
