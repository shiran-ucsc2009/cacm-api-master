package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportCustomerQueryDetailDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportTransactionQueryDetailDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClientDataRepository {

    List<Map<String, Object>> executeQuery(String sql);

    List<ReportEtfOutDTO> executeEftOutQuery(String sql);

    List<ReportEtfDTO> executeEftInQuery(String sql);

    STRReportTransactionQueryDetailDTO executeSTRReportTransactionQuery(String sql);

    STRReportCustomerQueryDetailDTO executeSTRReportCustomerQuery(String sql);

    List<CTRReportDTO> executeCTRReport(String sql);



}
