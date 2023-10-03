package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportCustomerQueryDetailDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportTransactionQueryDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ClientDataRepositoryImpl implements ClientDataRepository {

    private final JdbcTemplate clientJdbcTemplate;

    @Autowired
    public ClientDataRepositoryImpl(JdbcTemplate clientJdbcTemplate) {
        this.clientJdbcTemplate = clientJdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> executeQuery(final String sql) {
        return this.clientJdbcTemplate.queryForList(sql);
    }

    @Override
    public List<ReportEtfOutDTO> executeEftOutQuery(String sql) {
        List<ReportEtfOutDTO> reportEtfOutDTOS=this.clientJdbcTemplate.query(sql,new BeanPropertyRowMapper(ReportEtfOutDTO.class));
        return reportEtfOutDTOS;
    }

    @Override
    public List<ReportEtfDTO> executeEftInQuery(String sql) {
        return this.clientJdbcTemplate.query(sql,new BeanPropertyRowMapper(ReportEtfDTO.class));
    }


    @Override
    public STRReportTransactionQueryDetailDTO executeSTRReportTransactionQuery(String sql) {

        return (STRReportTransactionQueryDetailDTO) this.clientJdbcTemplate.queryForObject(
                sql,new BeanPropertyRowMapper(STRReportTransactionQueryDetailDTO.class));

    }

    @Override
    public STRReportCustomerQueryDetailDTO executeSTRReportCustomerQuery(String sql) {
        return (STRReportCustomerQueryDetailDTO) this.clientJdbcTemplate.queryForObject(
                sql,new BeanPropertyRowMapper(STRReportCustomerQueryDetailDTO.class));
    }

    @Override
    public List<CTRReportDTO> executeCTRReport(String sql) {
        return  this.clientJdbcTemplate.query(
                sql,new BeanPropertyRowMapper(CTRReportDTO.class));
    }
}
