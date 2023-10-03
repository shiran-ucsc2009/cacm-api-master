package com.kpmg.cacm.api.schedule.spring;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.service.CategoryService;
import com.kpmg.cacm.api.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncidentAutoAssignSchedule {

    private final IncidentService incidentService;

    private final CategoryService categoryService;

    @Autowired
    public IncidentAutoAssignSchedule(final IncidentService incidentService, final CategoryService categoryService) {
        this.incidentService = incidentService;
        this.categoryService = categoryService;
    }

    // TODO : refactor this method with a worker thread
    public void assignOpenIncidents() {
        final Calendar calender = Calendar.getInstance();
        final List<IncidentStatus> status = Collections.singletonList(IncidentStatus.OPEN);
        this.incidentService.findAll(this.categoryService.findAll(), null, null, status,null).forEach(incident -> {
            calender.setTime(new Date());
            calender.add(Calendar.DATE, incident.getException().getExceptionDefinitionRun().getResolveDayCount());
            this.incidentService.batchAssign(
                Collections.singletonList(incident.getId()),
                incident.getException().getExceptionDefinitionRun().getDefaultAssignee().getId(),
                calender.getTime(),
                null
            );
        });
    }
}
