/* PRODUCT : CACM
 * PROJECT : cacm-api
 * PACKAGE : com.kpmg.cacm.api.analytics.service
 * ************************************************************************************************
 *
 * Copyright(C) 2019 KPMG Technology Service All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF KPMG Technology solution.
 *
 * This copy of the Source Code is intended for KPMG Technology solution's internal use only
 * and is intended for view by persons duly authorized by the management of KPMG Technology solution. No
 * part of this file may be reproduced or distributed in any form or by any
 * means without the written approval of the Management of KPMG Technology solution.
 *
 * *************************************************************************************************
 *
 * REVISIONS:
 * Author : kperera5
 * Date : 11/18/2019 - 3:50 PM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.analytics.service;

import com.kpmg.cacm.api.service.ClientDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: kperera5
 * Date : 11/18/2019
 */
@Service
public class ClientAnalyticsServiceImpl implements ClientAnalyticsService {

    private final ClientDataService clientDataService;
    public List<Map<String, Object>> accounts;
    public List<Map<String, Object>> selectedAccnts;

    @Value("${cacm.config.analytics.link-analysis-enable}")
    private Boolean isLinkAnalysisEnable;


    @Autowired
    public ClientAnalyticsServiceImpl(ClientDataService clientDataService) {
        this.clientDataService = clientDataService;
    }


    @Override
    public List<Map<String, Object>> findAll() {
        String sqlQuery="select id,nic_number,CONCAT(first_name,' ',last_name) as full_name,first_name,last_name," +
                        "occupation,customer_number from customer limit 100";

        List<Map<String, Object>> customers=this.clientDataService.executeClientQuery(sqlQuery);
        return customers;
    }

    @Override
    public List<Map<String, Object>> findAllCustomerByNic(String nic) {
        String sqlQuery="select id,nic_number,CONCAT(first_name,' ',last_name) as full_name,first_name,last_name, " +
                "occupation,customer_number from customer "+
                "where nic_number like "+"'"+nic+"%'";
        List<Map<String, Object>> customer=this.clientDataService.executeClientQuery(sqlQuery);
        return customer;
    }

    @Override
    public List<Map<String, Object>> findAllCustomerByFirstName(String firstName) {
        String sqlQuery="select id,nic_number,CONCAT(first_name,' ',last_name) as full_name,first_name,last_name, " +
                "occupation,customer_number from customer "+
                "where lower(first_name) like "+"'"+firstName+"%'";
        List<Map<String, Object>> customer=this.clientDataService.executeClientQuery(sqlQuery);
        return customer;
    }

    @Override
    public List<Map<String, Object>> findCustomerById(Long id) {
        String customerId= String.valueOf(id);
        String sqlQuery="select id,nic_number,CONCAT(first_name,' ',last_name) as full_name,first_name,last_name, " +
                        "occupation,customer_number from customer " +
                        " where id ="+customerId;
        List<Map<String, Object>> customer=this.clientDataService.executeClientQuery(sqlQuery);
        return customer;
    }

    @Override
    public List<Map<String, Object>> findAllAccountsById(Long id) {
        String customerId= String.valueOf(id);
        String sqlQuery="select id,account_type_name,account_number,account_status,sub_product_name ,"
                        + " primary_customer_id_id  from account "
                        + " where primary_customer_id_id = "+customerId;

         accounts=this.clientDataService.executeClientQuery(sqlQuery);
        return accounts;
    }

    @Override
    public List<Map<String, Object>> selectedAccountById(List<String> accounts) {
        String accountsList = String.join(",", accounts);
        String sqlQuery="select id,account_type_name,account_number,account_status,sub_product_name ,"
                + " primary_customer_id_id  from account "
                + " where id in  ("+accountsList+")";

        System.out.println(sqlQuery);

        selectedAccnts=this.clientDataService.executeClientQuery(sqlQuery);
        return selectedAccnts;
    }

    @Override
    public List<Map<String, Object>> findAllTransactions(Long id, List<String> accounts, String fromDate, String toDate) {
        String accountId=String.valueOf(id);
        String accountsList = String.join(",", accounts);

        String sqlQuery="select t.id,c.customer_number,c.first_name,c.last_name,c.nic_number,a.account_number,a.account_type_name,a.product_name," +
                        " a.branch_name,t.trn_type,t.currency_amount,t.trn_currency_name,t.trn_number,trn_date from customer c " +
                        " inner join account a " +
                        " on c.id = a.primary_customer_id_id " +
                        " inner join transaction_table t " +
                        " on a.id=t.account_id_id  " +
                        " where c.id = "+accountId
                        + " and a.id in ("+accountsList+")"
                        + " and t.trn_date between "+"'"+ fromDate+ "'"+" and "+"'"+ toDate+"'";

        List<Map<String, Object>> transactions=this.clientDataService.executeClientQuery(sqlQuery);
        return transactions;
    }

    @Override
    public Boolean isLinkAnalysisEnable() {
        return isLinkAnalysisEnable;
    }

}
