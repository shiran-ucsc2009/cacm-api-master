package com.kpmg.cacm.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.cacm.api.dto.request.STRReportRequest;
import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportCustomerQueryDetailDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportQueryDTO;
import com.kpmg.cacm.api.dto.response.model.STRReportTransactionQueryDetailDTO;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.model.ReportingOfficer;
import com.kpmg.cacm.api.repository.spring.ClientDataRepository;
import com.kpmg.cacm.api.repository.spring.IncidentRepository;
import com.kpmg.cacm.api.repository.spring.ReportingOfficerRepository;
import com.kpmg.cacm.api.repository.spring.UserRepository;
import com.kpmg.cacm.api.service.ClientDataService;
import com.kpmg.cacm.api.service.ReportService;
import com.kpmg.cacm.api.util.ReportSQLStatements;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    private final ClientDataService clientDataService;

    private final ClientDataRepository clientDataRepository;

    private final IncidentRepository incidentRepository;

    private final ReportingOfficerRepository reportingOfficerRepository;

    public ReportServiceImpl(final IncidentRepository incidentRepository,
                             final ClientDataRepository clientDataRepository,
                             final ClientDataService clientDataService,
                             final UserRepository userRepository,
                             final ReportingOfficerRepository reportingOfficerRepository) {
        this.incidentRepository = incidentRepository;
        this.clientDataRepository= clientDataRepository;
        this.clientDataService = clientDataService;
        this.userRepository = userRepository;
        this.reportingOfficerRepository = reportingOfficerRepository;
    }

    private  JdbcTemplate jdbcTemplate;


    @Override
    public List<ReportEtfDTO> getEtfReportDetail(String fromDate, String toDate) {

        String sql="select  '54' as BankCode," +
                " fd.trn_date   as ValueDate," +
                "   COALESCE(fd.amount,0)  as ValueFCY," +
                "    'LKR' as Curcode," +
                "   COALESCE(fd.amount,0) as ValueRs," +
                "  COALESCE(tr.trn_type,'')   as TxnDetails," +
                "    '' as SenderName," +
                "    '' as SenderAddress," +
                "    '' as SenderBusiness," +
                "    '' as SenderBankBIC," +
                "    '' as SenderBankName," +
                "    '' as SenderBankAddress," +
                "    'LB FINANCE PLC' as ReceiverBank," +
                "   COALESCE(fd.account_number,'')   as ReceiverAccNo," +
                "  COALESCE(c.nic_number,'')  as ReceiverID," +
                "  COALESCE(c.current_address1,'')  as ReceiverAddress1," +
                "  COALESCE(c.current_address2,'')   as ReceiverAddress2," +
                "  COALESCE(c.current_address3,'')  as ReceiverAddress3," +
                "  COALESCE(c.occupation,'')  as ReceiverBusiness," +
                "    '' as TranReference " +
                "       from transaction_table tr " +
                "inner join fd_entries fd " +
                "    on tr.account_id=fd.account_number " +
                "inner join customer c " +
                "    on fd.customer_number=c.customer_number " +
                " where fd.renew_date between '"+fromDate+"' and '"+toDate+"'" +
                "and fd.txnid='RN'";

        return this.clientDataRepository.executeEftInQuery(sql);
    }


    @Override
    public List<ReportEtfOutDTO> getEtfOutReportsDetail(String fromDate, String toDate) throws IOException {


        String sql1 = "select  '54' as BankCode," +
                " fd.trn_date   as ValueDate," +
                "   COALESCE(fd.amount,0)  as ValueFCY," +
                "    'LKR' as Curcode," +
                "   COALESCE(fd.amount,0) as ValueRs," +
                "  COALESCE(tr.trn_type,'')   as TxnDetails," +
                "  ''  as SenderName," +
                "  ''   as SenderAccNo," +
                "  ''  as SenderID," +
                "    '' as SenderAddress1," +
                "    '' as SenderAddress2," +
                "    '' as SenderAddress3," +
                "    '' as SenderBusiness," +
                "     COALESCE(fd.account_number,'') as ReceiverAccNo," +
                "     COALESCE(CONCAT(c.first_name,'   ' , c.last_name),'')     as ReceiverName," +
                "    COALESCE(CONCAT(c.current_address1,  c.current_address2, c.current_address3) ,'')     as ReceiverAddress," +
                "    'LB FINANCE PLC'     as ReceiverBankName," +
                " '' as ReceiverBankBIC " +
                "       from transaction_table tr " +
                "inner join fd_entries fd " +
                "    on tr.account_id=fd.account_number " +
                "inner join customer c " +
                "    on fd.customer_number=c.customer_number " +
                " where tr.trn_date between '" + fromDate + "' and '" + toDate + "'" +
                "and fd.txnid='MD' union all ";


        String sql = "select  distinct " +
                "    '54' as BankCode," +
                "    tr.trn_date as ValueDate," +
                "   COALESCE(tr.currency_amount,0)   as ValueFCY," +
                "    'LKR' as Curcode," +
                "  COALESCE(tr.currency_amount,0)    as ValueRs," +
                "  COALESCE(tr.trn_type,'')    as TxnDetails," +
                "  COALESCE(ec.account_name,'')    as SenderName," +
                "  COALESCE(ec.account_code,'')    as SenderAccNo," +
                "  COALESCE(ec.module,'')   as SenderID," +
                "    '' as SenderAddress1," +
                "    '' as SenderAddress2," +
                "    '' as SenderAddress3," +
                "    '' as SenderBusiness," +
                "     COALESCE(tr.account_id,'') as ReceiverAccNo," +

                "     COALESCE(ec.account_name,'')     as ReceiverName," +
                "    COALESCE(ec.country_name,'')     as ReceiverAddress," +
                "    COALESCE( b.bank_name,'')     as ReceiverBankName," +
                " '' as ReceiverBankBIC " +
                "from transaction_table_slips tr " +

                "    inner join external_customers ec on tr.beneficiary_ext_party_id=ec.id " +
                " inner join customer c2 on tr.execting_party_id=c2.id " +
                " left join bank b on ec.bankcode=b.bank_code " +
                " where tr.trn_date between '" + fromDate + "' and '" + toDate + "'";

        String eftoutQuery = sql1 + sql;
        return this.clientDataRepository.executeEftOutQuery(eftoutQuery);
    }

    @Transactional(rollbackFor = java.lang.Exception.class)
    @Override
    public STRReportDTO genarateSTRreport(STRReportRequest strReportRequest) {
        Incident incident = this.incidentRepository.findByIdAndDeletedFalse(strReportRequest.getIncidentId());
        String json_transaction_id = incident.getQueryDataJson();
         String customer_id = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        STRReportDTO strReportDTO = new STRReportDTO();
        try {
            if (strReportRequest.getIsSubQuery()) {
                Object q = strReportRequest.getSubQueryData();
                List list= java.util.Arrays.asList(q);

                customer_id =incident.getClientCustomer().getCustomerId();

                for (STRReportQueryDTO strReportQueryDTO : strReportRequest.getColumnNames()) {
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Account No")) {

                        String finalQueryColumn = strReportQueryDTO.getQueryColumn();
                        ((LinkedHashMap) list.get(0)).forEach((key, value) -> {
                            if (finalQueryColumn.equalsIgnoreCase(key.toString())) {
                                strReportDTO.setAccountNo(value.toString());

                            }
                        });

                    }
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Amount")) {

                        String finalQueryColumn = strReportQueryDTO.getQueryColumn();
                        ((LinkedHashMap) list.get(0)).forEach((key, value) -> {
                            if (finalQueryColumn.equalsIgnoreCase(key.toString())) {
                                strReportDTO.setRupeesValue(Double.parseDouble(value.toString()));
                                strReportDTO.setAmountInFCY(Double.parseDouble(value.toString()));

                            }
                        });

                    }
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Transaction Date")) {

                        String finalQueryColumn = strReportQueryDTO.getQueryColumn();
                        ((LinkedHashMap) list.get(0)).forEach((key, value) -> {
                            if (finalQueryColumn.equalsIgnoreCase(key.toString())) {
                                strReportDTO.setTransactionDate(value.toString());
                            }
                        });

                    }
                }

            } else{

                for (STRReportQueryDTO strReportQueryDTO : strReportRequest.getColumnNames()) {
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Account No")) {

                        node = mapper.readTree(json_transaction_id);
                        String accNo = node.get(strReportQueryDTO.getQueryColumn()).asText();
                        strReportDTO.setAccountNo(accNo);

                    }
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Amount")) {

                        node = mapper.readTree(json_transaction_id);
                        String amount = node.get(strReportQueryDTO.getQueryColumn()).asText();
                        strReportDTO.setRupeesValue(Double.parseDouble(amount));
                        strReportDTO.setAmountInFCY(Double.parseDouble(amount));

                    }
                    if (strReportQueryDTO.getColumn().equalsIgnoreCase("Transaction Date")) {

                        node = mapper.readTree(json_transaction_id);
                        String trx_date = node.get(strReportQueryDTO.getQueryColumn()).asText();
                        strReportDTO.setTransactionDate(trx_date);

                    }

                }
        }
        STRReportTransactionQueryDetailDTO strReportTransactionQueryDetailDTO = new STRReportTransactionQueryDetailDTO();
        STRReportCustomerQueryDetailDTO strReportCustomerQueryDetailDTO = new STRReportCustomerQueryDetailDTO();

            node = mapper.readTree(json_transaction_id);
            customer_id = node.get("customer_id").asText();

            strReportCustomerQueryDetailDTO = this.clientDataRepository.executeSTRReportCustomerQuery(String.format(ReportSQLStatements.informationOnCustomer, customer_id));

            strReportDTO.setBankCode(54);
            strReportDTO.setName(strReportCustomerQueryDetailDTO.getFirst_name() +" "+ strReportCustomerQueryDetailDTO.getLast_name());
            strReportDTO.setOtherName(strReportCustomerQueryDetailDTO.getFirst_name() +" "+ strReportCustomerQueryDetailDTO.getLast_name());
            strReportDTO.setOccupation(strReportCustomerQueryDetailDTO.getOccupation());
            strReportDTO.setAddress1(strReportCustomerQueryDetailDTO.getPermanent_address1());
            strReportDTO.setAddress2(strReportCustomerQueryDetailDTO.getPermanent_address2());
            strReportDTO.setAddress3(strReportCustomerQueryDetailDTO.getPermanent_address3());
            strReportDTO.setCountry(strReportCustomerQueryDetailDTO.getPermanent_address_country_name());
            strReportDTO.setIdNumber(strReportCustomerQueryDetailDTO.getNic_number());
            strReportDTO.setEmployementType(" ");
            strReportDTO.setEmployer(strReportCustomerQueryDetailDTO.getEmployer_name());
            strReportDTO.setTelNo(strReportCustomerQueryDetailDTO.getPhone_number());
            strReportDTO.setLastRenewDate(strReportCustomerQueryDetailDTO.getLast_modified_date());

            strReportDTO.setAccountType(" ");
            strReportDTO.setDateOpened(strReportTransactionQueryDetailDTO.getOpen_date());
            strReportDTO.setCurrentBalance(strReportTransactionQueryDetailDTO.getCurrency_amount());
            strReportDTO.setCurrencyCode(strReportTransactionQueryDetailDTO.getTrn_currency_name());
            strReportDTO.setBranchName(strReportTransactionQueryDetailDTO.getBranch_name());
            strReportDTO.setBranchAddress(strReportTransactionQueryDetailDTO.getBranch_address());
            strReportDTO.setAccountStatus(strReportTransactionQueryDetailDTO.getAccount_status());
            strReportDTO.setFrequency("Frequency");

            ReportingOfficer reportingOfficer = this.reportingOfficerRepository.findByDeletedFalse();

            strReportDTO.setReportingOfficerName(reportingOfficer.getName());
            strReportDTO.setDesignation(reportingOfficer.getDesignation());
            strReportDTO.setAddress(reportingOfficer.getAddress());
            strReportDTO.setReportingOfficeTelNo(reportingOfficer.getTelephone());
            strReportDTO.setFax(reportingOfficer.getFax());
            strReportDTO.setEmail(reportingOfficer.getEmail());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return strReportDTO;
    }

    @Override
    public List<CTRReportDTO> genarateCTRReport(String fromDate, String toDate) {
        Stream<String> anStream = Stream.of(ReportSQLStatements.CTRSavings, ReportSQLStatements.CTRFDCashDeposit,ReportSQLStatements.CTRFDLoan,ReportSQLStatements.CTRFDCAshWithdrawal,ReportSQLStatements.CTRGLGoldLoan,ReportSQLStatements.CTRGLCAshDeposit,ReportSQLStatements.CTRGoldLoanL);
        String[] trxSqls={ReportSQLStatements.CTRSavings, ReportSQLStatements.CTRFDCashDeposit,ReportSQLStatements.CTRFDLoan,ReportSQLStatements.CTRFDCAshWithdrawal};//,,ReportSQLStatements.CTRGLGoldLoan,ReportSQLStatements.CTRGLCAshDeposit,ReportSQLStatements.CTRGoldLoanL};
        List<CTRReportDTO> ctrAllTrxReportDTOS=new ArrayList<>();
        List<CTRReportDTO> ctrReportDTOS = new ArrayList<>();
        String formattedSql =null;
        List<String> resultList = anStream.collect(Collectors.toList());
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("fromDate",fromDate);
        queryParams.put("toDate",toDate);
        for (int q=0;q<trxSqls.length;q++){
             formattedSql = new StrSubstitutor(queryParams).replace(trxSqls[q]);
             ctrReportDTOS= clientDataRepository.executeCTRReport(formattedSql);
            ctrAllTrxReportDTOS.addAll(ctrReportDTOS);
        }


        return ctrAllTrxReportDTOS;
    }
}
