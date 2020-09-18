package com.antra.evaluation.reporting_system;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.*;
import com.antra.evaluation.reporting_system.service.ExcelGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportingSystemApplicationTests {

    @Autowired
    ExcelGenerationService reportService;

    ExcelData data = new ExcelData();
    ExcelRequest er = new ExcelRequest();
    MultiSheetExcelRequest er_ms = new MultiSheetExcelRequest();

    @BeforeEach // We are using JUnit 5, @Before is replaced by @BeforeEach
    public void setUpData() {
        data.setTitle("Test book");
        data.setGeneratedTime(LocalDateTime.now());

        var sheets = new ArrayList<ExcelDataSheet>();
        var sheet1 = new ExcelDataSheet();
        sheet1.setTitle("First Sheet");

        var headersS1 = new ArrayList<ExcelDataHeader>();
        ExcelDataHeader header1 = new ExcelDataHeader();
        header1.setName("NameTest");
        //       header1.setWidth(10000);
        header1.setType(ExcelDataType.STRING);
        headersS1.add(header1);

        ExcelDataHeader header2 = new ExcelDataHeader();
        header2.setName("Age");
        //   header2.setWidth(10000);
        header2.setType(ExcelDataType.NUMBER);
        headersS1.add(header2);

        List<List<Object>> dataRows = new ArrayList<>();
        List<Object> row1 = new ArrayList<>();
        row1.add("Dawei");
        row1.add(12);
        List<Object> row2 = new ArrayList<>();
        row2.add("Dawei2");
        row2.add(23);
        dataRows.add(row1);
        dataRows.add(row2);

        sheet1.setDataRows(dataRows);
        sheet1.setHeaders(headersS1);
        sheets.add(sheet1);
        data.setSheets(sheets);

        var sheet2 = new ExcelDataSheet();
        sheet2.setTitle("second Sheet");
        sheet2.setDataRows(dataRows);
        sheet2.setHeaders(headersS1);
        sheets.add(sheet2);

        // request er
        er.setDescription("this is my file");
        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Age");
        er.setHeaders(headers);

        List<List<Object>> data = new ArrayList<>();
        List<Object> data1 = new ArrayList<>();
        data1.add("Teresa");
        data1.add("5");
        List<Object> data2 = new ArrayList<>();
        data2.add("Daniel");
        data2.add("1");
        data.add(data1);
        data.add(data2);
        er.setData(data);
        er.setSplitBy("Age");
        er.setSubmitter("John");

        // request er_ms
        er_ms.setDescription("this is my file_multiSheet");
        List<String> headers2 = new ArrayList<>();
        headers2.add("Name");
        headers2.add("Class");
        er_ms.setHeaders(headers2);

        List<List<Object>> data_ms = new ArrayList<>();
        List<Object> data_ms1 = new ArrayList<>();
        data_ms1.add("Teresa");
        data_ms1.add("A");
        List<Object> data_ms2 = new ArrayList<>();
        data_ms2.add("Daniel");
        data_ms2.add("B");
        List<Object> data_ms3 = new ArrayList<>();
        data_ms3.add("Ben");
        data_ms3.add("A");
        data_ms.add(data_ms1);
        data_ms.add(data_ms2);
        data_ms.add(data_ms3);
        er_ms.setData(data_ms);
        er_ms.setSplitBy("Class");
        er_ms.setSubmitter("John");
    }

    @Test
    public void testExcelGegeration() {
        File file = null;
        try {
            file = reportService.generateExcelReport(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(file != null);
    }

    @Test
    public void testGenerationAndSaveExcelFile(){
        ReturnExcelFileType reft = null;
        try{
            reft = reportService.generateAndSaveExcelFile(er);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(reft != null);

    }
    @Test
    public void testGenerationAndSaveExcelFileMultiSheets(){
        ReturnExcelFileType reft = null;
        try{
            reft = reportService.generateAndSaveMultiSheetsExcelFile(er_ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(reft != null);

    }

    @Test
    public void testTestCast(){
        int i = 10;
        boolean res = reportService.TestCase(i);
        assertTrue(res == true);
    }
}
