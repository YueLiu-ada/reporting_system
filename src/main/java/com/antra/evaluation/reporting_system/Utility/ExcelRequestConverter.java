package com.antra.evaluation.reporting_system.Utility;

import com.antra.evaluation.reporting_system.exception.MethodArgumentNotValid;
import com.antra.evaluation.reporting_system.exception.SpliterCannotFindException;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataHeader;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataSheet;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataType;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelRequestConverter {
    // this is for single page
    public static ExcelData convertExcelRequestToExcelData(ExcelRequest excelRequest){
        ExcelData excelData = new ExcelData();
        ExcelDataSheet excelDataSheet = new ExcelDataSheet();
        String des = excelRequest.getDescription(); // des as title
        // ExcelData's title is same with ExcelDataSheet'title，both of them are Excelrequest's description
        excelDataSheet.setTitle(des);
        excelData.setTitle(des);

        // give number to data row
        excelDataSheet.setDataRows(excelRequest.getData());

        List<String> tmp_headers = excelRequest.getHeaders();
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : tmp_headers){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);// default each type is string
            tmp.setWidth(head.length()); // set heade's length
            tmp_data_headers.add(tmp); // add to ExceDataHeader
        }
        // List<header>  to  ExcelDataSheet
        excelDataSheet.setHeaders(tmp_data_headers);
        // get time to ExcelData
        LocalDateTime time = LocalDateTime.now();
        excelData.setGeneratedTime(time);

        // ExcelDataSheet to ExcelData
        List<ExcelDataSheet> list = new ArrayList<>();
        list.add(excelDataSheet);
        excelData.setSheets(list);

        return excelData;
       // return null;
    }


    public static ExcelData convertExcelRequestToExcelData_MultiSheets(MultiSheetExcelRequest excelRequest){
        ExcelData excelData = new ExcelData();
        String des = excelRequest.getDescription(); // des as title
        // set exceldata title
        excelData.setTitle(des);

        String splitby = excelRequest.getSplitBy();
        List<String> headers = excelRequest.getHeaders();
        int col = -1;
        for(int i = 0; i < headers.size(); i++){
            if(headers.get(i).equals(splitby)){
                col = i;
            }
        }
        // col is split column
        // if not found
        if(col == -1) {
            // if cannot find, throw exception here
            throw new SpliterCannotFindException("we cannot find spliter");
        }
        List<List<Object>> list = excelRequest.getData();
        Map<Object,Integer> map = new HashMap<>();
        int cnt = 1;
        for(int i = 0; i < list.size(); i++){
            Object obj = list.get(i).get(col);
            // record different Obj
            if(!map.containsKey(obj)){
                map.put(obj,cnt);
                cnt++;
            }
        }
        // data header all of them are same
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : excelRequest.getHeaders()){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);// each type is string
            tmp.setWidth(head.length()); // set  head length
            tmp_data_headers.add(tmp); // add them to ExceDataHeader
        }

        List<ExcelDataSheet> excelDataSheetList = new ArrayList<>();
        // record number of different objs
        for(int i = 1; i < cnt; i++){
            // each page needs a sheet
            ExcelDataSheet excelDataSheet = new ExcelDataSheet();
            Object obj = null;
            for(Object tmp_obj : map.keySet()){
                if(map.get(tmp_obj) == i){
                    obj = tmp_obj;
                    break;
                }
            }
            // find the obj on the i th sheet
            // create a data sheet
            List<List<Object>> excelDataSheet_dataRows = new ArrayList<>();
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).get(col).equals(obj)){
                    // find elem add into data row
                    excelDataSheet_dataRows.add(new ArrayList<>(list.get(j)));
                }
            }
            // data rows save into excel data sheet
            excelDataSheet.setDataRows(excelDataSheet_dataRows);
            // data sheet  title  is ：sheet + i
            excelDataSheet.setTitle("sheet" + String.valueOf(i));
            excelDataSheet.setHeaders(tmp_data_headers);
            excelDataSheetList.add(excelDataSheet);
        }
        //  List<ExcelDataSheet> excelDataSheetList add into ExcelData
        excelData.setSheets(excelDataSheetList);
        // set excelData time
        excelData.setGeneratedTime(LocalDateTime.now());
        return excelData;
    }

    // validation data is logic or not.
    public static boolean ExcelRequestValidation(ExcelRequest excelRequest){
        if(excelRequest.getDescription() == null || excelRequest.getDescription().length() == 0){
            throw new MethodArgumentNotValid("description should not be null or 0 length");
        }
        if(excelRequest.getHeaders() == null || excelRequest.getHeaders().size() == 0){
            throw new MethodArgumentNotValid("headers should not be null or 0 length");
        }
        if(excelRequest.getData() == null || excelRequest.getData().size() == 0){
            throw new MethodArgumentNotValid("you should provide data");
        }
        if(excelRequest.getData().get(0) == null || excelRequest.getData().get(0).size() != excelRequest.getHeaders().size()){
            throw new MethodArgumentNotValid("you should provide data has same number of columns with headers");
        }
        return true;
    }

    public static boolean ExcelRequestMultiSheetValidation(MultiSheetExcelRequest multiSheetExcelRequest){
        if(multiSheetExcelRequest.getData().get(0) == null || multiSheetExcelRequest.getData().get(0).size() != multiSheetExcelRequest.getHeaders().size()){
            throw new MethodArgumentNotValid("you should provide data has same number of columns with headers");
        }
        return true;
    }

}
