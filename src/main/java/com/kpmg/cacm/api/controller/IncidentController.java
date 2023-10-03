package com.kpmg.cacm.api.controller;

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
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

@RestController
public interface IncidentController {

    @Secured("FIND_ALL_INCIDENTS")
    @GetMapping("/exceptions/incidents")
    IncidentResponse findAll(
            @RequestParam(value = "exception", required = false) Long exceptionId,
            @RequestParam(value = "customer", required = false) Long customerId,
            @RequestParam(value = "status", required = false) String incidentStatus,
            @RequestParam(value = "customer-risk", required = false) String customerRisk
    );

    @Secured("FIND_INCIDENT_BY_ID")
    @GetMapping("/exceptions/incidents/{id}")
    IncidentResponse findById(@PathVariable("id") Long id);

    @Secured("ASSIGN_INCIDENT")
    @PutMapping("/exceptions/incidents/actions/batch-assign")
    EmptyResponse batchAssign(@Valid @RequestBody IncidentBatchAssignRequest incidentBatchAssignReq);

    @Secured("BATCH_APPROVE_INCIDENT")
    @PutMapping("/exceptions/incidents/actions/batch-approve")
    EmptyResponse batchApprove(@Valid @RequestBody IncidentBatchApproveRequest batchApproveRequest);

    @Secured("RESOLVE_INCIDENT")
    @PostMapping("/exceptions/incidents/{id}/actions/resolve")
    EmptyResponse resolve(@RequestParam(value = "attachmentFile",required = false) MultipartFile file, IncidentResolveRequest incidentResolveReq, @PathVariable("id") Long incidentId);

    @Secured("REJECT_INCIDENT")
    @PutMapping("/exceptions/incidents/{id}/actions/reject")
    EmptyResponse reject(@Valid @RequestBody IncidentRejectRequest incidentRejectReq, @PathVariable("id") Long incidentId);

    @Secured("APPROVE_INCIDENT")
    @PutMapping("/exceptions/incidents/{id}/actions/approve")
    EmptyResponse approve(@Valid @RequestBody IncidentApproveRequest incidentApprovalReq, @PathVariable("id") Long incidentId);

    @Secured("OVERRIDE_INCIDENT_APPROVAL")
    @PutMapping("/exceptions/incidents/{id}/actions/override-approval")
    EmptyResponse overrideApproval(@Valid @RequestBody IncidentApproveRequest incidentApprovalReq, @PathVariable("id") Long incidentId);

    @Secured("FINALIZE_INCIDENT")
    @PutMapping("/exceptions/incidents/{id}/actions/finalize")
    EmptyResponse finalize(@Valid @RequestBody IncidentFinalizeRequest incidentFinalizeReq, @PathVariable("id") Long incidentId);

    @Secured("BATCH_FINALIZE_INCIDENT")
    @PutMapping("/exceptions/incidents/actions/batch-finalize")
    EmptyResponse batchFinalize(@Valid @RequestBody IncidentBatchFinalizedRequest batchFinalizedRequest);

    @Secured("REJECT_INCIDENT_APPROVAL")
    @PutMapping("/exceptions/incidents/{id}/actions/reject-approval")
    EmptyResponse rejectApproval(@Valid @RequestBody IncidentRejectApprovalRequest incidentRejectApprovalReq, @PathVariable("id") Long incidentId);

    @Secured("SET_INCIDENT_DUE_DATE")
    @PutMapping("/exceptions/incidents/{id}/actions/set-due-date")
    EmptyResponse setDueDate(@Valid @RequestBody IncidentSetDueDateRequest setDueDateReq, @PathVariable("id") Long incidentId);

    @Secured("COUNT_INCIDENTS")
    @GetMapping("/exceptions/incidents/count")
    CountResponse countAll(@RequestParam(value = "status", required = false) IncidentStatus incidentStatus);

    @Secured("INCIDENT_AGING_BY_STATUS")
    @GetMapping("/exceptions/incidents/aging-by-status")
    IncidentAgingResponse agingByStatus();

    @Secured("FIND_ALL_INCIDENTS_BY_DATEDIFF")
    @GetMapping("/exceptions/incidents/aging")
    IncidentResponse findAllIncidentByDateDiff(@RequestParam("date-diff") Integer dateDiff,@RequestParam(value = "status", required = false) String incidentStatus);

    @Secured("FIND_ALL_INCIDENT_BY_DATE_RANGE")
    @GetMapping("exceptions/incidents/finalized")
    IncidentResponse findAllIncidentByDateRange(
            @RequestParam(value = "status",required = false) String incidentStatus,
            @RequestParam(value = "category", required = false) String categoryName,
            @RequestParam("fromDate")
            @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate")
            @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate
    );

    @Secured("INCIDENT_GRAPH_BY_CATEGORY")
    @GetMapping("/exceptions/incidents/incident-by-categories")
    IncidentGraphByCategoryResponse incidentByCategory() throws ParseException;

    @Secured("FALSE_POSITIVE_BY_ACTION")
    @GetMapping("/exceptions/incidents/incident-by-action")
    FalsePositiveByActionsResponse incidentByAction();

    @Secured("INCIDENT_GRAPH_BY_PRIORITY")
    @GetMapping("/exceptions/incidents/incident-by-priority")
    IncidentGraphByPriorityResponse incidentByPriority();

    @Secured("OWNER_WISE_INCIDENT_COUNT")
    @GetMapping("/exceptions/incidents/owner-wise-count")
    IncidentGraphByOwnerResponse ownerrWiseCount();

    @Secured("RISK_MATRIX_COUNT")
    @GetMapping("/exceptions/incidents/risk_matrix-count")
    RiskMatrixResponse riskMatrixCount();

    @Secured("RISK_MATRIX_EXCEPTION_DTO")
    @GetMapping("/exceptions/incidents/risk-matrix-exception-dto")
    RiskMatrixResponse riskMatrixExceptionDto(
            @RequestParam(value = "customer-risk",required = false) String customerRisk,
            @RequestParam(value = "scenario-risk", required = false) String scenarioRisk
    );


    @Secured("FIND_ALL_INCIDENTS")
    @PostMapping("/exceptions/incidents/dt")
    DataTablesOutput<IncidentDTO> findAllByStatusDt(
        @Valid @RequestBody DataTablesInput dataTablesInput,
        @RequestParam(value = "exception", required = false) Long exceptionId,
        @RequestParam(value = "customer", required = false) Long customerId,
        @RequestParam(value = "status", required = false) String incidentStatus,
        @RequestParam(value = "customer-risk",required = false) Priority customerRisk,
        @RequestParam(value = "scenario-risk", required = false) Priority scenarioRisk
    );

    @Secured("VALIDATE_INCIDENT_CATEGORY")
    @PostMapping("/exceptions/incidents/incidents-category")
    IncidentBatchCategoryValidateResponse validateIncidentCategory(@Valid @RequestBody IncidentBatchCategoryValidateRequest incidentBatchCategoryValidateRequest);

}
