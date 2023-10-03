package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.IncidentController;
import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.dto.constant.Priority;
import com.kpmg.cacm.api.dto.request.IncidentApproveRequest;
import com.kpmg.cacm.api.dto.request.IncidentBatchApproveRequest;
import com.kpmg.cacm.api.dto.request.IncidentBatchAssignRequest;
import com.kpmg.cacm.api.dto.request.IncidentBatchCategoryValidateRequest;
import com.kpmg.cacm.api.dto.request.IncidentBatchFinalizedRequest;
import com.kpmg.cacm.api.dto.request.IncidentFinalizeRequest;
import com.kpmg.cacm.api.dto.request.IncidentRejectApprovalRequest;
import com.kpmg.cacm.api.dto.request.IncidentRejectRequest;
import com.kpmg.cacm.api.dto.request.IncidentResolveRequest;
import com.kpmg.cacm.api.dto.request.IncidentSetDueDateRequest;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.IncidentAgingResponse;
import com.kpmg.cacm.api.dto.response.IncidentBatchCategoryValidateResponse;
import com.kpmg.cacm.api.dto.response.IncidentGraphByCategoryResponse;
import com.kpmg.cacm.api.dto.response.IncidentGraphByOwnerResponse;
import com.kpmg.cacm.api.dto.response.IncidentGraphByPriorityResponse;
import com.kpmg.cacm.api.dto.response.IncidentResponse;
import com.kpmg.cacm.api.dto.response.RiskMatrixResponse;
import com.kpmg.cacm.api.dto.response.FalsePositiveByActionsResponse;
import com.kpmg.cacm.api.dto.response.model.IncidentDTO;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.service.IncidentService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidentControllerImpl implements IncidentController {

    private final IncidentService incidentService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public IncidentControllerImpl(
            final IncidentService incidentService,
            final UserService userService,
            final ModelMapper modelMapper
    ) {
        this.incidentService = incidentService;
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    @Override
    public IncidentResponse findAll(final Long exceptionId, final Long customerId, final String incidentStatus, final String customerRisk) {
        final List<IncidentStatus> statuses = getIncidentStatuses(incidentStatus);
        return IncidentResponse.builder().incidents(this.incidentService.findAll(
            this.userService.getCurrentUser().getCategories(),
            exceptionId,
            customerId,
            statuses,
                customerRisk
        ).stream().map(incident -> this.modelMapper.map(incident, IncidentDTO.class)).collect(Collectors.toList())).build();
    }

    private List<IncidentStatus> getIncidentStatuses(String incidentStatus) {
        final List<IncidentStatus> statuses;
        if (incidentStatus == null) {
            statuses = null;
        } else if ("all".equals(incidentStatus)) {
            statuses = new ArrayList<IncidentStatus>(){{
                this.add(IncidentStatus.OPEN);
                this.add(IncidentStatus.ASSIGNED);
                this.add(IncidentStatus.RESOLVED);
                this.add(IncidentStatus.APPROVED);
            }};
        } else {
            statuses = Collections.singletonList(IncidentStatus.valueOf(incidentStatus.toUpperCase()));
        }
        return statuses;
    }

    @Override
    public IncidentResponse findById(final Long id) {
        final Incident incident = this.incidentService.findById(id);
        if (!this.userService.getCurrentUser().getCategories().contains(incident.getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        return IncidentResponse.builder().incidents(
            Collections.singletonList(this.modelMapper.map(incident, IncidentDTO.class))
        ).build();
    }

    @Override
    public EmptyResponse batchAssign(final IncidentBatchAssignRequest incidentBatchAssignReq) {
        final List<Category> categories = this.userService.getCurrentUser().getCategories();
        incidentBatchAssignReq.getIncidentIds().stream().map(this.incidentService::findById).forEach(incident -> {
            if (!categories.contains(incident.getCategory())) {
                throw new NotAuthorizedException("Not authorized to access one or more resources in specified category");
            }
        });


        this.incidentService.batchAssign(
            incidentBatchAssignReq.getIncidentIds(),
            incidentBatchAssignReq.getUserId(),
            incidentBatchAssignReq.getDueDate(),
            incidentBatchAssignReq.getComment()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse resolve(MultipartFile file, final IncidentResolveRequest incidentResolveReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.resolve(file,
            incidentId,
            incidentResolveReq.getIncidentCauseId(),
            incidentResolveReq.getComment()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse reject(final IncidentRejectRequest incidentRejectReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.rejectResolution(
            incidentId,
            incidentRejectReq.getUserId(),
            incidentRejectReq.getComment(),
            incidentRejectReq.getDueDate()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse approve(final IncidentApproveRequest incidentApprovalReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.approveResolution(
            incidentId,
            incidentApprovalReq.getApproveAction(),
            incidentApprovalReq.getComment(),
                incidentApprovalReq.getIncidentApproveReasons()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse batchApprove(@Valid IncidentBatchApproveRequest batchApproveRequest) {
        final List<Category> categories = this.userService.getCurrentUser().getCategories();
        batchApproveRequest.getIncidentIds().stream().map(this.incidentService::findById).forEach(incident -> {
            if (!categories.contains(incident.getCategory())) {
                throw new NotAuthorizedException("Not authorized to access specified category");
            }
        });

        this.incidentService.batchApprove(
                batchApproveRequest.getIncidentIds(),
                batchApproveRequest.getApproveAction(),
                batchApproveRequest.getComment(),
                batchApproveRequest.getIncidentApproveReasons()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse overrideApproval(final IncidentApproveRequest incidentApprovalReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.overrideApproval(
            incidentId,
            incidentApprovalReq.getApproveAction(),
                incidentApprovalReq.getComment(),
                incidentApprovalReq.getIncidentApproveReasons()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse finalize(@Valid final IncidentFinalizeRequest incidentFinalizeReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.finalizeApproval(
            incidentId,
            incidentFinalizeReq.getComment()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse batchFinalize(@Valid IncidentBatchFinalizedRequest batchFinalizedRequest) {
        final List<Category> categories = this.userService.getCurrentUser().getCategories();
        batchFinalizedRequest.getIncidentIds().stream().map(this.incidentService::findById).forEach(incident -> {
            if (!categories.contains(incident.getCategory())) {
                throw new NotAuthorizedException("Not authorized to access specified category");
            }
        });
        this.incidentService.batchFinalize(
                batchFinalizedRequest.getIncidentIds(),
                batchFinalizedRequest.getComment()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse rejectApproval(@Valid IncidentRejectApprovalRequest incidentRejectApprovalReq, Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.rejectApproval(
            incidentId,
            incidentRejectApprovalReq.getComment(),
            incidentRejectApprovalReq.getDueDate()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public EmptyResponse setDueDate(@Valid final IncidentSetDueDateRequest setDueDateReq, final Long incidentId) {
        if (!this.userService.getCurrentUser().getCategories().contains(this.incidentService.findById(incidentId).getCategory())) {
            throw new NotAuthorizedException("Not authorized to access specified category");
        }
        this.incidentService.setDueDate(
            incidentId,
            setDueDateReq.getDueDate()
        );
        return EmptyResponse.builder().build();
    }

    @Override
    public CountResponse countAll(final IncidentStatus incidentStatus) {
        return CountResponse.builder().count(this.incidentService.countAllByCategoryAndStatusAndClientCustomer(
            this.userService.getCurrentUser().getCategories(),
            incidentStatus,
            null
        )).build();
    }

    @Override
    public IncidentAgingResponse agingByStatus() {
        return IncidentAgingResponse.builder().incidentList(
            this.incidentService.getIncidentAgingDetails()
        ).build();

    }

    @Override
    public IncidentResponse findAllIncidentByDateDiff(Integer dateDiff,String incidentStatus) {
        final List<IncidentStatus> statuses = getIncidentStatuses(incidentStatus);
        return IncidentResponse.builder().incidents(
            this.incidentService.findAllIncidentByAge(dateDiff,statuses).stream()
                .map(incident -> this.modelMapper.map(incident, IncidentDTO.class))
                .collect(Collectors.toList())
        ).build();
    }

    @Override
    public IncidentResponse findAllIncidentByDateRange(String incidentStatus, String categoryName, Date fromDate, Date toDate) {

        List<Category> categories = null;
        if (categoryName != null) {
            for (final Category category : this.userService.getCurrentUser().getCategories()) {
                if(category.getName().equals(categoryName)) {
                    categories = Collections.singletonList(category);
                }
            }
        } else {
            categories = this.userService.getCurrentUser().getCategories();
        }
        if (categories == null) {
            throw new NotAuthorizedException("Not authorized to access any specified categories");
        }
        final List<IncidentDTO> incidents = this.incidentService.findAllIncidentsByDateRange(IncidentStatus.valueOf(incidentStatus.toUpperCase()),categories,fromDate,toDate)
            .stream()
            .map(incident -> this.modelMapper.map(incident, IncidentDTO.class))
            .collect(Collectors.toList());
        return IncidentResponse.builder().incidents(incidents).build();
    }


    @Override
    public IncidentGraphByCategoryResponse incidentByCategory() throws ParseException {
        return  IncidentGraphByCategoryResponse.builder().IncidentList(
            this.incidentService.getIncidentByCategory()
        ).build();
    }

    @Override
    public FalsePositiveByActionsResponse incidentByAction() {
        return FalsePositiveByActionsResponse.builder().IncidentList(
              this.incidentService.getIncidentByAction()
        ).build();
    }

    @Override
    public IncidentGraphByPriorityResponse incidentByPriority() {
        return  IncidentGraphByPriorityResponse.builder().incidentGraphByPriorityDTO(
            this.incidentService.getIncidentByPriority()
        ).build();
    }

    @Override
    public IncidentGraphByOwnerResponse ownerrWiseCount() {
        return IncidentGraphByOwnerResponse.builder().incidentGraphByOwnerDTO(
            new ArrayList<>(this.incidentService.getIncidentCountByOwner(
                this.userService.getCurrentUser().getCategories()
            ))
        ).build();
    }

    @Override
    public RiskMatrixResponse riskMatrixCount() {
        return RiskMatrixResponse.builder().riskMatrixDTOS(
                new ArrayList<>(this.incidentService.riskMatrixCount(
                        this.userService.getCurrentUser().getCategories()
                ))
        ).build();
    }

    @Override
    public RiskMatrixResponse riskMatrixExceptionDto(String customerRisk,String scenarioRisk) {
        return RiskMatrixResponse.builder().riskMatrixExceptionDTOS(new ArrayList<>(this.incidentService.riskMatrixExceptionList(
                this.userService.getCurrentUser().getCategories(),
                Priority.valueOf(customerRisk.toUpperCase()),
                Priority.valueOf(scenarioRisk.toUpperCase())
        ))).build();
    }

    @Override
    public DataTablesOutput<IncidentDTO> findAllByStatusDt(@Valid DataTablesInput dataTablesInput, Long exceptionId, Long customerId, String incidentStatus,Priority customerRisk,Priority scenarioRisk) {
        final List<IncidentStatus> statuses = getIncidentStatuses(incidentStatus);
        return this.incidentService.findAllByCategoriesAndStatusDt(
            this.userService.getCurrentUser().getCategories(),
            statuses,
            customerId,
            exceptionId,
                customerRisk,
                scenarioRisk,
            dataTablesInput,
            incident -> this.modelMapper.map(incident, IncidentDTO.class)
        );
    }

    @Override
    public IncidentBatchCategoryValidateResponse validateIncidentCategory(@Valid IncidentBatchCategoryValidateRequest incidentBatchCategoryValidateRequest) {
        List<Category> incidentCategories =  new ArrayList<>();
        incidentBatchCategoryValidateRequest.getIncidentIds().stream().map(this.incidentService::findById).forEach(incident -> {
            incidentCategories.add(incident.getCategory());
        });
        long incidentCategoryMatch = incidentCategories.stream().distinct().count();

        return IncidentBatchCategoryValidateResponse.builder().incidentCategoryMatch(incidentCategoryMatch).build();
    }
}