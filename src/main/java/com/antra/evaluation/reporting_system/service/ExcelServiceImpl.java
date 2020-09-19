package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    ExcelRepository excelRepository;

    @Override
    public InputStream getExcelBodyById(String id) throws FileNotFoundException {

        ExcelFile fileInfo = excelRepository.getFileById(id);
        if(fileInfo == null) {
            throw new FileNotFoundException("we do not have this file");
        }
            File file = new File(fileInfo.getFile());
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return null;
    }

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
