package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.Utility.ExcelRequestConverter;
import com.antra.evaluation.reporting_system.exception.NullRepoException;
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

    // delete file in map according to Id you provided
    // no such Id return null
    // have id , delete
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
