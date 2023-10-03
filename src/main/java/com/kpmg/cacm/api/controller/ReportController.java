package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.request.STRReportRequest;
import com.kpmg.cacm.api.dto.response.STRReportResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public interface ReportController {

    @Secured("DOWNLOAD_BANK_REPORT")
    @GetMapping("/download/customers.xlsx/{report-type}/{fromdate}/{todate}")
    ResponseEntity<InputStreamResource> excelEFTReport(
            @PathVariable("report-type") String reportType,
            @PathVariable("fromdate") String fromDate,
            @PathVariable("todate") String toDate) throws IOException;

    @Secured("GENERATE_STR_REPORT")
    @PostMapping("/generate_str")
    STRReportResponse genarateSTRReport(@Valid @RequestBody STRReportRequest strReportRequest);
}