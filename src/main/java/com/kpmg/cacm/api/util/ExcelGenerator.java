package com.kpmg.cacm.api.util;

import com.kpmg.cacm.api.dto.response.model.CTRReportDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfDTO;
import com.kpmg.cacm.api.dto.response.model.ReportEtfOutDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelGenerator {



    public static ByteArrayInputStream EftInToExcel(List<ReportEtfDTO> reportEtfDTOS) throws IOException {

        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){


            Sheet sheet = workbook.createSheet("EFT IN");
            sheet.setDefaultColumnWidth(30);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("BankCode");
            header.createCell(1).setCellValue("ValueDate");
            header.createCell(2).setCellValue("ValueFCY");
            header.createCell(3).setCellValue("CurCode");
            header.createCell(4).setCellValue("ValueRs");
            header.createCell(5).setCellValue("TxnDetails");
            header.createCell(6).setCellValue("SenderName");
            header.createCell(7).setCellValue("SenderAddress");
            header.createCell(8).setCellValue("SenderBusiness");
            header.createCell(9).setCellValue("SenderBankBIC");
            header.createCell(10).setCellValue("SenderBankName");
            header.createCell(11).setCellValue("SenderBankAddress");
            header.createCell(12).setCellValue("ReceiverBank");
            header.createCell(13).setCellValue("ReceiverAccNo");
            header.createCell(14).setCellValue("ReceiverID");
            header.createCell(15).setCellValue("ReceiverAddress1");
            header.createCell(16).setCellValue("ReceiverAddress2");
            header.createCell(17).setCellValue("ReceiverAddress3");
            header.createCell(18).setCellValue("ReceiverBusiness");
            header.createCell(19).setCellValue("TranReference");

            // CellStyle
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            int rowCount = 1;
            for(ReportEtfDTO user : reportEtfDTOS){
                String valueDate = sdf.format(user.getValueDate());
                Row userRow =  sheet.createRow(rowCount++);
                userRow.createCell(0).setCellValue(user.getBankCode());
                userRow.createCell(1).setCellValue(valueDate);
                userRow.createCell(2).setCellValue(user.getValueFCY());
                userRow.createCell(3).setCellValue(user.getCurcode());
                userRow.createCell(4).setCellValue(user.getValueRs());
                userRow.createCell(5).setCellValue(user.getTxnDetails());
                userRow.createCell(6).setCellValue(user.getSenderName());
                userRow.createCell(7).setCellValue(user.getSenderAddress());
                userRow.createCell(8).setCellValue(user.getSenderBusiness());
                userRow.createCell(9).setCellValue(user.getSenderBankBIC());
                userRow.createCell(10).setCellValue(user.getSenderBankName());
                userRow.createCell(11).setCellValue(user.getSenderBankAddress());
                userRow.createCell(12).setCellValue(user.getReceiverBank());
                userRow.createCell(13).setCellValue(user.getReceiverAccNo());
                userRow.createCell(14).setCellValue(user.getReceiverID());
                userRow.createCell(15).setCellValue(user.getReceiverAddress1());
                userRow.createCell(16).setCellValue(user.getReceiverAddress2());
                userRow.createCell(17).setCellValue(user.getReceiverAddress3());
                userRow.createCell(18).setCellValue(user.getReceiverBusiness());
                userRow.createCell(19).setCellValue(user.getTranReference());

            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }
    }

    public static ByteArrayInputStream EftOutToExcel(List<ReportEtfOutDTO>customers) throws IOException {
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            Sheet sheet = workbook.createSheet("EfT Out");
            sheet.setDefaultColumnWidth(30);

        //    System.out.println(customers.get(0).size());
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("BankCode");
            header.createCell(1).setCellValue("ValueDate");
            header.createCell(2).setCellValue("ValueFCY");
            header.createCell(3).setCellValue("CurCode");
            header.createCell(4).setCellValue("ValueRs");
            header.createCell(5).setCellValue("TxnDetails");
            header.createCell(6).setCellValue("SenderName");
            header.createCell(7).setCellValue("SenderAccNo");
            header.createCell(8).setCellValue("SenderID");
            header.createCell(9).setCellValue("SenderAddress1");
            header.createCell(10).setCellValue("SenderAddress2");
            header.createCell(11).setCellValue("SenderAddress3");
            header.createCell(12).setCellValue("SenderBusiness");
            header.createCell(13).setCellValue("ReceiverAccNo");
            header.createCell(14).setCellValue("ReceiverName");
            header.createCell(15).setCellValue("ReceiverAddress");
            header.createCell(16).setCellValue("ReceiverBankName");
            header.createCell(17).setCellValue("ReceiverBankBIC");
            // CellStyle
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat( createHelper.createDataFormat().getFormat("yyyyMMdd"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            int rowCount = 1;
            for(ReportEtfOutDTO etf : customers){
                String valueDate = sdf.format(etf.getValueDate());
                Row EtfOutRow =  sheet.createRow(rowCount++);
                EtfOutRow.createCell(0).setCellValue(etf.getBankCode());
                EtfOutRow.createCell(1).setCellValue(valueDate);
                EtfOutRow.createCell(2).setCellValue(etf.getValueFCY());
                EtfOutRow.createCell(3).setCellValue(etf.getCurcode());
                EtfOutRow.createCell(4).setCellValue(etf.getValueRs());
                EtfOutRow.createCell(5).setCellValue(etf.getTxnDetails());
                EtfOutRow.createCell(6).setCellValue(etf.getSenderName());
                EtfOutRow.createCell(7).setCellValue(etf.getSenderAccNo());
                EtfOutRow.createCell(8).setCellValue(etf.getSenderID());
                EtfOutRow.createCell(9).setCellValue(etf.getSenderAddress1());
                EtfOutRow.createCell(10).setCellValue(etf.getSenderAddress2());
                EtfOutRow.createCell(11).setCellValue(etf.getSenderAddress3());
                EtfOutRow.createCell(12).setCellValue(etf.getSenderBusiness());
                EtfOutRow.createCell(13).setCellValue(etf.getReceiverAccNo());
                EtfOutRow.createCell(14).setCellValue(etf.getReceiverName());
                EtfOutRow.createCell(15).setCellValue(etf.getReceiverAddress());
                EtfOutRow.createCell(16).setCellValue(etf.getReceiverBankName());
                EtfOutRow.createCell(17).setCellValue(etf.getReceiverBankBIC());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }
    }


    public static ByteArrayInputStream CTROutToExcel(List<CTRReportDTO>ctrReportDTOS) throws IOException {
        String[] CTRReportColumns={"BankCode","BranchCode","BranchName","AccNumber",
                "ValueDate","CurCode","Amount","CrDr",
                "AccType","TxnNature","CusFirstName","CusLastName",
                "AddressLine1","AddressLine2","AddressLine3","IDNumber",
                "Tel","Fax", "Email","RiskRating","BusinessType","CusType","AccStatus", "Nationality"
        };
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("CTR");
        sheet.setDefaultColumnWidth(30);
        Row header = sheet.createRow(0);
        for (int q=0;q<CTRReportColumns.length;q++){

            header.createCell(q).setCellValue(CTRReportColumns[q]);
        }

        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat( createHelper.createDataFormat().getFormat("yyyyMMdd"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        int rowCount = 1;
        for(CTRReportDTO ctr : ctrReportDTOS){
            String valueDate = sdf.format(ctr.getValueDate());
            Row CTROutRow =  sheet.createRow(rowCount++);
            CTROutRow.createCell(0).setCellValue(ctr.getBankCode());
            CTROutRow.createCell(1).setCellValue(ctr.getBranchCode());
            CTROutRow.createCell(2).setCellValue(ctr.getBranchName());
            CTROutRow.createCell(3).setCellValue(ctr.getAccNumber());
            CTROutRow.createCell(4).setCellValue(ctr.getValueDate());
            CTROutRow.createCell(5).setCellValue(ctr.getCurCode());
            CTROutRow.createCell(6).setCellValue(ctr.getAmount());
            CTROutRow.createCell(7).setCellValue(ctr.getCrDr());
            CTROutRow.createCell(8).setCellValue(ctr.getAccType());
            CTROutRow.createCell(9).setCellValue(ctr.getTxnNature());
            CTROutRow.createCell(10).setCellValue(ctr.getCusFirstName());
            CTROutRow.createCell(11).setCellValue(ctr.getCusLastName());
            CTROutRow.createCell(12).setCellValue(ctr.getAddressLine1());
            CTROutRow.createCell(13).setCellValue(ctr.getAddressLine2());
            CTROutRow.createCell(14).setCellValue(ctr.getAddressLine3());
            CTROutRow.createCell(15).setCellValue(ctr.getIdNumber());
            CTROutRow.createCell(16).setCellValue(ctr.getTel());
            CTROutRow.createCell(17).setCellValue(ctr.getFax());
            CTROutRow.createCell(18).setCellValue(ctr.getEmail());
            CTROutRow.createCell(19).setCellValue(ctr.getRiskRating());
            CTROutRow.createCell(20).setCellValue(ctr.getBusinessType());
            CTROutRow.createCell(21).setCellValue(ctr.getCusType());
            CTROutRow.createCell(22).setCellValue(ctr.getAccStatus());
            CTROutRow.createCell(23).setCellValue(ctr.getNationality());

        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
