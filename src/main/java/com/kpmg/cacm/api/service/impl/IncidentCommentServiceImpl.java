package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kpmg.cacm.api.dto.constant.IncidentHistoryRecordType;
import com.kpmg.cacm.api.model.IncidentComment;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.repository.spring.IncidentCommentRepository;
import com.kpmg.cacm.api.service.IncidentCommentService;
import com.kpmg.cacm.api.service.IncidentHistoryService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentCommentServiceImpl implements IncidentCommentService {

    private final IncidentCommentRepository incidentCommentRepository;
    private final UserService userService;
    private final IncidentHistoryService incidentHistoryService;

    @Autowired
    public IncidentCommentServiceImpl(
            final IncidentCommentRepository incidentCommentRepository,
            final UserService userService,
            final IncidentHistoryService incidentHistoryService) {
        this.incidentCommentRepository = incidentCommentRepository;
        this.userService = userService;
        this.incidentHistoryService = incidentHistoryService;
    }

    @Override
    public IncidentComment findById(final Long id) {
        return this.incidentCommentRepository.findByIdAndDeletedFalse(id);
    }

    protected void save(final IncidentComment incidentComment) {
        final User commentUser = this.userService.getCurrentUser();
        incidentComment.setUser(commentUser);
        this.incidentCommentRepository.save(incidentComment);
    }

    @Override
    public void add(final IncidentComment incidentComment) {
        final Map<String, String> commentParam = new HashMap<>(16);
        commentParam.put("comment", incidentComment.getComment());
        this.incidentHistoryService.addRecord(incidentComment.getIncident(), IncidentHistoryRecordType.COMMENT, commentParam);
        this.save(incidentComment);
    }

    @Override
    public void deleteById(final Long id) {
        final IncidentComment incidentComment = this.findById(id);
        incidentComment.setDeleted(true);
        incidentComment.setDeletionToken(new Date().getTime());
        this.save(incidentComment);
    }

    @Override
    public List<IncidentComment> findAllByIncidentId(final Long incidentId) {
        return this.incidentCommentRepository.findAllByIncident_IdAndDeletedFalse(incidentId);
    }
}
