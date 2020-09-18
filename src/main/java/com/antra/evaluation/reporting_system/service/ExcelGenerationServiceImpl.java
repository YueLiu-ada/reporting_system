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
import java.util.Date;
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
    // 检验 exceldata 是否符合标准？
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

    // 作用是产生excel文件
    @Override
    public File generateExcelReport(ExcelData data) throws IOException {
        // 先检验data是否符合标准
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
        //先把ExcelRequest转化成ExcelData
        ExcelRequestConverter.ExcelRequestValidation(request);
        File file = generateExcelReport(ExcelRequestConverter.convertExcelRequestToExcelData(request));
        if(!file.exists() || !file.isFile()){
            throw new FileCannotSaveException("fail to generate excel file");
        }
        // 然后让generator根据这个data生成一个Excel文件
        String id = file.getName();
        String abs_path = file.getAbsolutePath();
        String fileID = excelRepository.saveFile(id, abs_path);

        ReturnExcelFileType reft = new ReturnExcelFileType();
        reft.setFileId(fileID);
        reft.setAbs_path(abs_path);
        reft.setComplete_time(LocalDateTime.now().toString());// set time
        long file_size = file.length();
        reft.setFile_size(String.valueOf(file_size) + "Byte");
        return reft;
    }

    @Override
    public ReturnExcelFileType generateAndSaveMultiSheetsExcelFile(MultiSheetExcelRequest request) throws IOException{
        File file = generateExcelReport(ExcelRequestConverter.convertExcelRequestToExcelData_MultiSheets(request));
        if(!file.exists() || !file.isFile()){
            throw new FileCannotSaveException("fail to generate multi-sheets excel file");
        }
        // 然后让generator根据这个data生成一个Excel文件
        String id = file.getName();
        String abs_path = file.getAbsolutePath();
        String fileID = excelRepository.saveFile(id, abs_path);

        ReturnExcelFileType reft = new ReturnExcelFileType();
        reft.setFileId(fileID);
        reft.setAbs_path(abs_path);
        reft.setComplete_time(LocalDateTime.now().toString());// set time
        long file_size = file.length();
        reft.setFile_size(String.valueOf(file_size) + "Byte");
        return reft;
        //return null;
    }
    @Override
    public boolean TestCase(int i){
        if(i > 0) return true;
        return false;
    }

}
