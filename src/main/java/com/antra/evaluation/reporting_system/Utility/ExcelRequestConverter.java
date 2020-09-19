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
    public static ExcelData convertExcelRequestToExcelData(ExcelRequest excelRequest){
        ExcelData excelData = new ExcelData();
        ExcelDataSheet excelDataSheet = new ExcelDataSheet();
        String des = excelRequest.getDescription();
        excelDataSheet.setTitle(des);
        excelData.setTitle(des);

        excelDataSheet.setDataRows(excelRequest.getData());

        List<String> tmp_headers = excelRequest.getHeaders();
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : tmp_headers){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);
            tmp.setWidth(head.length());
            tmp_data_headers.add(tmp);
        }
        excelDataSheet.setHeaders(tmp_data_headers);
        LocalDateTime time = LocalDateTime.now();
        excelData.setGeneratedTime(time);

        List<ExcelDataSheet> list = new ArrayList<>();
        list.add(excelDataSheet);
        excelData.setSheets(list);

        return excelData;
    }


    public static ExcelData convertExcelRequestToExcelData_MultiSheets(MultiSheetExcelRequest excelRequest){
        ExcelData excelData = new ExcelData();
        String des = excelRequest.getDescription();
        excelData.setTitle(des);

        String splitby = excelRequest.getSplitBy();
        List<String> headers = excelRequest.getHeaders();
        int col = -1;
        for(int i = 0; i < headers.size(); i++){
            if(headers.get(i).equals(splitby)){
                col = i;
            }
        }
        if(col == -1) {
            throw new SpliterCannotFindException("we cannot find spliter");
        }
        List<List<Object>> list = excelRequest.getData();
        Map<Object,Integer> map = new HashMap<>();
        int cnt = 1;
        for(int i = 0; i < list.size(); i++){
            Object obj = list.get(i).get(col);
            if(!map.containsKey(obj)){
                map.put(obj,cnt);
                cnt++;
            }
        }
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : excelRequest.getHeaders()){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);
            tmp.setWidth(head.length());
            tmp_data_headers.add(tmp);
        }

        List<ExcelDataSheet> excelDataSheetList = new ArrayList<>();
        for(int i = 1; i < cnt; i++){
            ExcelDataSheet excelDataSheet = new ExcelDataSheet();
            Object obj = null;
            for(Object tmp_obj : map.keySet()){
                if(map.get(tmp_obj) == i){
                    obj = tmp_obj;
                    break;
                }
            }
            List<List<Object>> excelDataSheet_dataRows = new ArrayList<>();
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).get(col).equals(obj)){
                    // find elem add into data row
                    excelDataSheet_dataRows.add(new ArrayList<>(list.get(j)));
                }
            }
            excelDataSheet.setDataRows(excelDataSheet_dataRows);
            excelDataSheet.setTitle("sheet" + String.valueOf(i));
            excelDataSheet.setHeaders(tmp_data_headers);
            excelDataSheetList.add(excelDataSheet);
        }
        excelData.setSheets(excelDataSheetList);
        excelData.setGeneratedTime(LocalDateTime.now());
        return excelData;
    }

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
