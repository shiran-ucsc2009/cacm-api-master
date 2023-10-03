package com.kpmg.cacm.api.service;

import com.kpmg.cacm.api.dto.request.STRReportRequest;
import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportDTO;

import java.io.IOException;
import java.util.List;

public interface ReportService {


 List<ReportEtfDTO> getEtfReportDetail(String fromDate, String toDate);

 List<ReportEtfOutDTO> getEtfOutReportsDetail(String fromDate, String toDate) throws IOException;

 STRReportDTO genarateSTRreport(STRReportRequest strReportRequest);

 List<CTRReportDTO> genarateCTRReport(String fromDate, String toDate);



}
