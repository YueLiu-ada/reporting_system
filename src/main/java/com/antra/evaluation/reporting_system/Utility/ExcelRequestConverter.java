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
    // 这个是只有一页的
    public static ExcelData convertExcelRequestToExcelData(ExcelRequest excelRequest){
        ExcelData excelData = new ExcelData();
        ExcelDataSheet excelDataSheet = new ExcelDataSheet();
        String des = excelRequest.getDescription(); // des as title
        // ExcelData 里面的title 和 ExcelDataSheet的title设置成一样的，都是Excelrequest的description
        excelDataSheet.setTitle(des);
        excelData.setTitle(des);

        // 赋值data row
        excelDataSheet.setDataRows(excelRequest.getData());

        List<String> tmp_headers = excelRequest.getHeaders();
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : tmp_headers){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);// 每个type都是string
            tmp.setWidth(head.length()); // 长度设置成 head的长度
            tmp_data_headers.add(tmp); // 把它加入到ExceDataHeader里面去
        }
        // List<header> 搞好了 赋值给 ExcelDataSheet
        excelDataSheet.setHeaders(tmp_data_headers);
        // 获取时间 赋值给ExcelData
        LocalDateTime time = LocalDateTime.now();
        excelData.setGeneratedTime(time);

        // ExcelDataSheet 赋值给 ExcelData
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
            // 记录出现过的不同的Obj
            if(!map.containsKey(obj)){
                map.put(obj,cnt);
                cnt++;
            }
        }
        // data header 所有sheet的应该都是一样的
        List<ExcelDataHeader> tmp_data_headers = new ArrayList<>();
        for(String head : excelRequest.getHeaders()){
            ExcelDataHeader tmp = new ExcelDataHeader();
            tmp.setName(head);
            tmp.setType(ExcelDataType.STRING);// 每个type都是string
            tmp.setWidth(head.length()); // 长度设置成 head的长度
            tmp_data_headers.add(tmp); // 把它加入到ExceDataHeader里面去
        }

        List<ExcelDataSheet> excelDataSheetList = new ArrayList<>();
        // 出现过几种不同的Obj就是有几页
        for(int i = 1; i < cnt; i++){
            // 每页都要创建一个sheet
            ExcelDataSheet excelDataSheet = new ExcelDataSheet();
            Object obj = null;
            for(Object tmp_obj : map.keySet()){
                if(map.get(tmp_obj) == i){
                    obj = tmp_obj;
                    break;
                }
            }
            // find the obj on the i th sheet
            // 这里是创建一个data sheet
            List<List<Object>> excelDataSheet_dataRows = new ArrayList<>();
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).get(col).equals(obj)){
                    // 找到符合的元素了 加入到data row 里面去
                    excelDataSheet_dataRows.add(new ArrayList<>(list.get(j)));
                }
            }
            // 现在已经加完这页的data rows 了 放到excel data sheet里面去
            excelDataSheet.setDataRows(excelDataSheet_dataRows);
            // 这个data sheet的title是：sheet + i
            excelDataSheet.setTitle("sheet" + String.valueOf(i));
            excelDataSheet.setHeaders(tmp_data_headers);
            excelDataSheetList.add(excelDataSheet);
        }
        // 现在 List<ExcelDataSheet> excelDataSheetList 完成了 加到ExcelData里
        excelData.setSheets(excelDataSheetList);
        // 设置excelData的时间
        excelData.setGeneratedTime(LocalDateTime.now());
        return excelData;
    }

    // validation manually
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
}
