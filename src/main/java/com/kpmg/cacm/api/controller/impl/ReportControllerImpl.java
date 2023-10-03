package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.ReportController;
import com.kpmg.cacm.api.dto.request.STRReportRequest;
import com.kpmg.cacm.api.dto.response.STRReportResponse;
import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import com.kpmg.cacm.api.service.ClientDataService;
import com.kpmg.cacm.api.service.ReportService;
import com.kpmg.cacm.api.util.ExcelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ReportControllerImpl implements ReportController {

    private final  ReportService reportService;

    @Autowired
    public ReportControllerImpl(final ClientDataService clientDataService,
                                    final ReportService reportService) {
       this.reportService = reportService;
    }

    @Override
    public ResponseEntity<InputStreamResource> excelEFTReport(String reportType, String fromDate, String toDate) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        ByteArrayInputStream in = null;
        if(reportType.equals("EFT-IN")){
             List<ReportEtfDTO> reportEtfDTOS= this.reportService.getEtfReportDetail(fromDate,toDate);
             in = ExcelGenerator.EftInToExcel(reportEtfDTOS);
            headers.add("Content-Disposition", "attachment; filename=eft-in.xlsx");

        }else if(reportType.equals("EFT-OUT")){
            List<ReportEtfOutDTO> reportEtfOutDTOS=this.reportService.getEtfOutReportsDetail(fromDate,toDate);
            in = ExcelGenerator.EftOutToExcel(reportEtfOutDTOS);
            headers.add("Content-Disposition", "attachment; filename=eft-out.xlsx");
        }else {
            String currentdate="2019-11-27";
            List<CTRReportDTO> reportCTROutDTOS=this.reportService.genarateCTRReport(fromDate,toDate);
            System.out.println("ctrReportDTOS ----"+reportCTROutDTOS);
            in = ExcelGenerator.CTROutToExcel(reportCTROutDTOS);
           headers.add("Content-Disposition", "attachment; filename=ctr.xlsx");
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));

    }


    @Override
    public STRReportResponse genarateSTRReport( STRReportRequest strReportRequest) {
        return STRReportResponse.builder().strReportDTO(this.reportService.genarateSTRreport(strReportRequest)).build();
    }

}
