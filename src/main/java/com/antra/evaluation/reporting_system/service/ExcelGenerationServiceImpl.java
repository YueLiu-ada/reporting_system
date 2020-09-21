package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.Utility.ExcelRequestConverter;
import com.antra.evaluation.reporting_system.exception.FileCannotSaveException;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.*;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Stucture
 * data - title, generatedTime
 * - sheets
 *      -sheet1 - title (required)
 *              - headers
 *                   - name
 *                   - width
 *                   - type
 *              - dataRows
 *                   - List of objects/values
 */
@Service
public class ExcelGenerationServiceImpl implements ExcelGenerationService {

    @Autowired
    ExcelRepository excelRepository;
    private void validateDate(ExcelData data) {
        if (data.getSheets().size() < 1) {
            throw new RuntimeException("Excel Data Error: no sheet is defined");
        }
        for (ExcelDataSheet sheet : data.getSheets()) {
            if (StringUtils.isEmpty(sheet.getTitle())) {
                throw new RuntimeException("Excel Data Error: sheet name is missing");
            }
            if(sheet.getHeaders() != null) {
                int columns = sheet.getHeaders().size();
                for (List<Object> dataRow : sheet.getDataRows()) {
                    if (dataRow.size() != columns) {
                        throw new RuntimeException("Excel Data Error: sheet data has difference length than header number");
                    }
                }
            }
        }
    }

    @Override
    public File generateExcelReport(ExcelData data) throws IOException {
        validateDate(data);
        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);


        for (ExcelDataSheet sheetData : data.getSheets()) {
            Sheet sheet = workbook.createSheet(sheetData.getTitle());

            Row header = sheet.createRow(0);
            List<ExcelDataHeader> headersData = sheetData.getHeaders();
            for (int i = 0; i < headersData.size(); i++) {
                ExcelDataHeader headerData = headersData.get(i);
                Cell headerCell = header.createCell(i);
                headerCell.setCellValue(headerData.getName());
                if(headerData.getWidth() > 0) sheet.setColumnWidth(i, headerData.getWidth());
                headerCell.setCellValue(headerData.getName());
                headerCell.setCellStyle(headerStyle);
            }
            var rowData = sheetData.getDataRows();
            for (int i = 0; i < rowData.size(); i++) {
                Row row = sheet.createRow(1 + i);
                var eachRow = rowData.get(i);
                for (int j = 0; j < eachRow.size(); j++) {
                    Cell cell = row.createCell(j);
//                    switch (headersData.get(j).getType()) {
//                        case STRING:cell.setCellValue(String.valueOf(eachRow.get(j))); cell.setCellType(CellType.STRING);break;
//                        case NUMBER: cell.setCellValue(eachRow.get(j));cell.setCellType(CellType.NUMERIC);break;
//                        case DATE:cell.setCellValue((Date)eachRow.get(j));break;
//                        default:cell.setCellValue(String.valueOf(eachRow.get(j)));break;
//                    }
                    cell.setCellValue(String.valueOf(eachRow.get(j)));
                    cell.setCellStyle(style);
                }
            }
            for (int i = 0; i < headersData.size(); i++) {
                sheet.autoSizeColumn(i);
            }
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String gen_time = data.getGeneratedTime().toString();
        String fileLocation = path.substring(0, path.length() - 1) + data.getTitle() + gen_time + "temp.xlsx";  // TODO : file name cannot be hardcoded here

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(fileLocation);
    }

    @Override
    public ReturnExcelFileType generateAndSaveExcelFile(ExcelRequest request) throws IOException {
        ExcelRequestConverter.ExcelRequestValidation(request);
        File file = generateExcelReport(ExcelRequestConverter.convertExcelRequestToExcelData(request));
        if(!file.exists() || !file.isFile()){
            throw new FileCannotSaveException("fail to generate excel file");
        }
        String id = file.getName();
        String absPath = file.getAbsolutePath();
        String fileID = excelRepository.saveFile(id, absPath);

        ReturnExcelFileType reft = new ReturnExcelFileType();
        reft.setFileId(fileID);
        reft.setAbsPath(absPath);
        reft.setCompleteTime(LocalDateTime.now().toString());
        long fileSize = file.length();
        reft.setFileSize(String.valueOf(fileSize) + "Byte");
        return reft;
    }

    @Override
    public ReturnExcelFileType generateAndSaveMultiSheetsExcelFile(MultiSheetExcelRequest request) throws IOException{
        ExcelRequestConverter.ExcelRequestMultiSheetValidation(request);
        File file = generateExcelReport(ExcelRequestConverter.convertExcelRequestToExcelData_MultiSheets(request));
        if(!file.exists() || !file.isFile()){
            throw new FileCannotSaveException("fail to generate multi-sheets excel file");
        }
        String id = file.getName();
        String absPath = file.getAbsolutePath();
        String fileID = excelRepository.saveFile(id, absPath);

        ReturnExcelFileType reft = new ReturnExcelFileType();
        reft.setFileId(fileID);
        reft.setAbsPath(absPath);
        reft.setCompleteTime(LocalDateTime.now().toString());
        long file_size = file.length();
        reft.setFileSize(String.valueOf(file_size) + "Byte");
        return reft;
    }

    @Override
    public List<ReturnExcelFileType> generateBatch(List<ExcelRequest> requestList) throws IOException {
        List<ReturnExcelFileType> reftList = new ArrayList<>();
        for(ExcelRequest excelRequest : requestList){
            if(excelRequest.getSplitBy() == null || excelRequest.getSplitBy().length() == 0){
                ReturnExcelFileType reft = generateAndSaveExcelFile(excelRequest);
                reftList.add(reft);
            }
            else{
                MultiSheetExcelRequest multiSheetExcelRequest = new MultiSheetExcelRequest();
                multiSheetExcelRequest.setData(excelRequest.getData());
                multiSheetExcelRequest.setDescription(excelRequest.getDescription());
                multiSheetExcelRequest.setHeaders(excelRequest.getHeaders());
                multiSheetExcelRequest.setSplitBy(excelRequest.getSplitBy());
                multiSheetExcelRequest.setSubmitter(excelRequest.getSubmitter());
                ReturnExcelFileType reft = generateAndSaveMultiSheetsExcelFile(multiSheetExcelRequest);
                reftList.add(reft);
            }
        }
        return reftList;
    }
}
