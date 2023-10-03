package com.kpmg.cacm.api.util;

public class ReportSQLStatements {

    static String trxID=null;

    static public final String transactionDetailsByTrxID="Select * from transaction_table where id=%s";//16992121
    static public final String accountDetailsByAcctID="Select * from account where  account_number=%s";//002030100006607

    //STR Report
    //Customer Details
    static public final String informationOnCustomer= "select first_name,last_name,occupation,permanent_address1,permanent_address2,permanent_address3,permanent_address_country_name,\n" +
            "nic_number,occupation_sector,employer_name,phone_number,last_modified_date\n" +
            "from client.public.customer\n" +
            "where customer_number='%s'";

    //Transaction Details FD
    static public final String transactionDetailsFD="Select acct.account_number,acct.account_type_name,acct.open_date,trxtable.trn_date,fd.amount\n" +
            ",trxtable.trn_currency_name,br.branch_name,br.branch_address,acct.account_status,trxtable.currency_amount\n" +
            "from account acct\n" +
            "inner join transaction_table trxtable   ON acct.account_number=trxtable.account_id\n" +
            "inner join fd_entries fd on fd.account_number=acct.account_number\n" +
            "inner join branch br      on br.branch_code=trxtable.branch_id\n" +
            "where fd.txnid='R' and  trxtable.id=%s";

    /*Select Savings Sub category*/
    static public final String savingsSubCategory="SELECT  sub_product_name\n" +
            "FROM account\n" +
            "  WHERE account_number =%s";


  /*  Savings Slip IN*/
    static public final String transactionDetailsSavingsSlipIN="Select acct.account_number,acct.account_type_name,acct.open_date,trxtable.trn_date,trxtable.outstanding_balance\n"+
            ",trxtable.trn_currency_name,br.branch_name,br.branch_address,acct.account_status,trxtable.currency_amount\n"+
            "from account acct\n"+
            "inner join transaction_table trxtable   ON acct.account_number=trxtable.account_id\n"+
            "inner join branch br      on br.branch_address=trxtable.branch_id and br.bank_id_id=803\n"+
            "where trxtable.id='%s'";


    //Savings Slip Out
    static public final String transactionDetailsSavingsSlipOut="Select acct.account_number,acct.account_type_name,acct.open_date,trxtable.trn_date,trxtable.outstanding_balance\\n\"+\n" +
            "            \",trxtable.trn_currency_name,br.branch_name,br.branch_address,acct.account_status,trxtable.currency_amount\\n\"+\n" +
            "            \"from account acct\\n\"+\n" +
            "            \"inner join transaction_table_slips trxtable   ON acct.account_number=trxtable.account_id\\n\"+\n" +
            "            \"inner join branch br      on br.branch_address=trxtable.branch_id and br.bank_id_id=803\\n\"+\n" +
            "            \"where trxtable.id='%s'";

    //Loans
    static public final String transactionDetailsLoan="";

    //Lease

    //Gold Loan


    /*
    CTR REPORT
     */

    //Savings - trx_table= module 'S'
     static public final String CTRSavings ="select '54' as BankCode ,br.branch_code as BranchCode ,br.branch_name  as branchName,trxtable.account_id as accNumber ,trxtable.trn_date as ValueDate,trxtable.trn_currency_name as curCode,\n"+
            "  trxtable.currency_amount as amount ,\n"+
            "  CASE WHEN trxtable.reference='C' THEN 'CR'\n"+
            "  WHEN trxtable.reference='D' THEN 'DR'\n"+
            "  else trxtable.reference\n"+
            "  END AS crDr\n"+
            ",\n"+
            "  CASE WHEN trxtable.reference='C' THEN 'CASH DEPOSIT'\n"+
            "  WHEN trxtable.reference='D' THEN 'CASH WITHDRAWAL'\n"+
            "  else trxtable.reference\n"+
            "  END AS accType\n"+
            ",\n"+
            "  CASE WHEN acct.account_status='Running' THEN 'ACTIVE'\n"+
            "  else 'INACTIVE'\n"+
            "  END AS accStatus\n"+
            ",\n"+
            " 'SAVING DEPOSIT' as TxnNature ,cus.first_name as  cusFirstName,cus.last_name as cusLastName,cus.current_address1 as addressLine1 ,cus.current_address2 as addressLine2 ,cus.current_address3 as addressLine3,cus.nic_number as idNumber,cus.phone_number as tel,\n"+
            "  cus.email as email,cus.risk_level as riskRating,cus.occupation_sector as businessType ,cus.customer_type_name as cusType,\n"+
            "  'SRI LANKAN' as Nationality\n"+
            "  from transaction_table trxtable\n"+
            "  inner join branch br      on  br.branch_address=trxtable.branch_id\n"+
            "  inner join customer cus on trxtable.execting_party_id = cus.id\n"+
            "  inner join account acct on trxtable.account_id=acct.account_number\n"+
            "  where module = 'S'\n"+
            "  and trn_date between '${fromDate}' and '${toDate}' and  br.bank_id_id=803";

    // Fixed Deposit-Cash Deposit  [trx_table= module 'F']
    static public final String CTRFDCashDeposit="select '54' as BankCode,br.branch_code as BranchCode,br.branch_name as branchName,\n" +
            "  COALESCE(acct.account_number,'') as accNumber,acct.open_date as ValueDate,COALESCE(acct.currency_name,'') as curCode,\n" +
            "  COALESCE(acct.original_loan_amount,0) as amount,\n" +
            "  'CR' as CrDr,\n" +
            "  'FIXED DEPOSIT' AccType,\n" +
            "  'CASH DEPOSIT' TxnNature,\n" +
            "  cus.first_name as  cusFirstName,cus.last_name as cusLastName,cus.current_address1 as addressLine1,cus.current_address2 as addressLine2,cus.current_address3 as addressLine3,cus.nic_number as idNumber,cus.phone_number as tel,\n" +
            "  cus.email as email,cus.risk_level  as riskRating,cus.occupation_sector as businessType,cus.customer_type_name  as cusType,\n" +
            "  'SRI LANKAN' as Nationality\n" +
            "  from account acct\n" +
            "  inner join branch br      on  br.branch_name=acct.branch_name\n" +
            "  inner join customer cus on acct.primary_customer_id_id  = cus.id\n" +
            "  inner join transaction_table trxtable on trxtable.account_id=acct.account_number\n" +
            "  where module = 'F'\n" +
            "  and trn_date between '${fromDate}' and '${toDate}' and  br.bank_id_id=803";



    // Fixed Deposit Loan [trx_table= module 'FL'
    static public final String CTRFDLoan="select '54' as BankCode,br.branch_code as BranchCode,br.branch_name as branchName,\n" +
            "  COALESCE(acct.account_number,'') as accNumber,acct.open_date as ValueDate,acct.currency_name  as curCode,\n" +
            "  COALESCE(acct.original_loan_amount,0) as amount,\n" +
            "  'CR' as CrDr,\n" +
            "  'FIXED DEPOSIT LOAN' AccType,\n" +
            "'CASH DEPOSIT' TxnNature,\n" +
            "cus.first_name as  cusFirstName,cus.last_name as cusLastName,cus.current_address1 as addressLine1,cus.current_address2  as addressLine2,cus.current_address3  as addressLine3,cus.nic_number as idNumber ,cus.phone_number as tel,\n" +
            "cus.email as email,cus.risk_level as riskRating,cus.occupation_sector as businessType,cus.customer_type_name as cusType,\n" +
            "'SRI LANKAN' as Nationality\n" +
            "from account acct\n" +
            "inner join branch br      on  br.branch_name=acct.branch_name\n" +
            "inner join customer cus on acct.primary_customer_id_id  = cus.id\n" +
            "inner join transaction_table trxtable on trxtable.account_id=acct.account_number\n" +
            "where module = 'FL'\n" +
            "and trn_date between '${fromDate}' and '${toDate}' and  br.bank_id_id=803";

    // Fixed Deposit-Cash Withdrawal  [fd-entries , txnid = 'WI' or 'PW']
    static public final String CTRFDCAshWithdrawal="select '54' as BankCode,br.branch_code as BranchCode,br.branch_name  as branchName,\n" +
            "COALESCE(fd.account_number,'')  as accNumber,fd.trn_date as ValueDate,'LKR' as CurCode,\n" +
            "COALESCE(fd.amount,0) as amount,\n" +
            "'CR' as CrDr,\n" +
            "'FIXED DEPOSIT LOAN' AccType,\n" +
            "'CASH DEPOSIT' TxnNature,\n" +
            "cus.first_name as  cusFirstName,cus.last_name  as cusLastName,cus.current_address1  as addressLine1,cus.current_address2  as addressLine2,cus.current_address3  as addressLine3,cus.nic_number as idNumber,cus.phone_number as tel,\n" +
            "cus.email  as email,cus.risk_level  as riskRating,cus.occupation_sector as businessType,cus.customer_type_name  as cusType,\n" +
            "'ACTIVE' as accstatus,\n" +
            "'SRI LANKAN' as Nationality\n" +
            "from fd_entries fd\n" +
            "inner join branch br      on  br.branch_address=fd.branchcode\n" +
            "inner join customer cus on fd.customer_number  = cus.customer_number\n" +
            "where txnid in  ('WI', 'PW') and br.bank_id_id=803\n" +
            "and trn_date between '${fromDate}' and '${toDate}'";

    // Gold loan [account .account _type name ='GOLD loan']
    static public final String CTRGLGoldLoan="";

    // Gold loan [account .account _type name ='CASH DEPOSIT']
    static public final String CTRGLCAshDeposit="";

    // Gold loan [trx_table= module 'L']
    static public final String CTRGoldLoanL="";


}
