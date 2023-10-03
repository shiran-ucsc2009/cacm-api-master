package com.kpmg.cacm.api.service.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.model.Exception;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.model.ClientCustomer;
import com.kpmg.cacm.api.repository.spring.ExceptionDefinitionRunRepository;
import com.kpmg.cacm.api.service.ClientCustomerService;
import com.kpmg.cacm.api.service.ClientDataService;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunService;
import com.kpmg.cacm.api.service.ExceptionService;
import com.kpmg.cacm.api.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExceptionDefinitionRunServiceImpl implements ExceptionDefinitionRunService {

    private final ClientDataService clientDataService;

    private final ExceptionService exceptionService;

    private final ClientCustomerService clientCustomerService;

    private final IncidentService incidentService;

    private final ObjectMapper objectMapper;

    private final ExceptionDefinitionRunRepository exceptionDefinitionRunRepository;

    @Value("${cacm.config.client.data.customer-id-field}")
    private String clientCustomerIdField;

    @Value("${cacm.config.client.data.customer-name-field}")
    private String clientCustomerNameField;

    @Value("customer_risk")
    private String clientCustomerRiskLevel;

    @Value("${cacm.config.incident.auto-assign}")
    private boolean autoAssignEnabled;

    @Autowired
    public ExceptionDefinitionRunServiceImpl(
        final ClientDataService clientDataService,
        final ExceptionService exceptionService,
        final ClientCustomerService clientCustomerService,
        final IncidentService incidentService,
        final ObjectMapper objectMapper,
        final ExceptionDefinitionRunRepository exceptionDefinitionRunRepository) {
        this.clientDataService = clientDataService;
        this.exceptionService = exceptionService;
        this.clientCustomerService = clientCustomerService;
        this.incidentService = incidentService;
        this.objectMapper = objectMapper;
        this.exceptionDefinitionRunRepository = exceptionDefinitionRunRepository;
    }

    @Override
    public ExceptionDefinitionRun save(final ExceptionDefinitionRun exceptionDefinitionRun) {
        return this.exceptionDefinitionRunRepository.save(exceptionDefinitionRun);
    }

    @Override
    public ExceptionDefinitionRun findById(final Long id) {
        return this.exceptionDefinitionRunRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<ExceptionDefinitionRun> findAll() {
        return this.exceptionDefinitionRunRepository.findAllByDeletedFalse();
    }

    @Override
    public long countAll() {
        return this.exceptionDefinitionRunRepository.countAllByDeletedFalse();
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void run(final ExceptionDefinitionRun exceptionDefRun, final String traceId) throws JsonProcessingException {

        final List<Map<String, Object>> incidentDataList = this.clientDataService.executeQuery(exceptionDefRun.getQuery());
        if (incidentDataList.size()<1) {
            return;
        }
        final Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.DATE, exceptionDefRun.getResolveDayCount());

        Exception exception = new Exception();
        exception.setExceptionDefinitionRun(exceptionDefRun);
        exception.setStatus(ExceptionStatus.OPEN);
        exception.setIncidentCount(incidentDataList.size());
        exception = this.exceptionService.save(exception);

        final List<Long> savedIncidentIds = new ArrayList<>();

        for (final Map<String, Object> incidentData : incidentDataList) {

            System.out.println("clientCustomerRiskLevel "+this.clientCustomerRiskLevel);
            final ClientCustomer customer = this.clientCustomerService.saveIfNotExists(
                incidentData.get(this.clientCustomerIdField).toString(),
                incidentData.get(this.clientCustomerNameField).toString(),
                    Long.parseLong( incidentData.get(this.clientCustomerRiskLevel).toString())
            );

            final Incident incident = new Incident();
            incident.setDueDate(calender.getTime());
            incident.setStatus(IncidentStatus.OPEN);
            incident.setAssignee(exceptionDefRun.getDefaultAssignee());
            incident.setCategory(exceptionDefRun.getCategory());
            incident.setException(exception);
            incident.setClientCustomer(customer);
            incident.setException(exception);
            incident.setQueryDataJson(this.objectMapper.writeValueAsString(incidentData));
            if(exceptionDefRun.getSubQuery() != null && !exceptionDefRun.getSubQuery().trim().isEmpty()) {
                incident.setSubQueryDataJson(this.objectMapper.writeValueAsString(
                    this.clientDataService.executeQuery(exceptionDefRun.getSubQuery(), incidentData)
                ));
            }
            this.incidentService.save(incident);
            if (this.autoAssignEnabled) {
                savedIncidentIds.add(incident.getId());
            }
        }

        if (!savedIncidentIds.isEmpty()) {
            this.incidentService.batchAssign(savedIncidentIds, exceptionDefRun.getDefaultAssignee().getId(), calender.getTime(), null);
        }
    }
}
