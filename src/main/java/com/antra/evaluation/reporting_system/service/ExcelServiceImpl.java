package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.Utility.ExcelRequestConverter;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    ExcelRepository excelRepository;

    @Override
    public InputStream getExcelBodyById(String id) {

        Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);
       // if (fileInfo.isPresent()) {
            File file = new File("temp.xlsx");
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
      //  }
        return null;
    }

    // 根据 ID 删除特定的file  从hashmap里删除
    // 如果本来就没这个ID 返回false
    // 如果本来有这个ID 并且成功删除 返回true
    @Override
    public boolean delExcelFileById(String id) {
        ExcelFile tmp = excelRepository.deleteFile(id);
        if(tmp == null){
            return false;
        }
        String abs_path = tmp.getFile();
        try{
            File file = new File(abs_path);
            if(file.delete()){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ExcelFileIdAndPath> listExistingFiles(){
        List<ExcelFileIdAndPath> res = excelRepository.getFiles();
        return res;
    }

}
