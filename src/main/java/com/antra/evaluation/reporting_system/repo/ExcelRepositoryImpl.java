package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ExcelRepositoryImpl implements ExcelRepository {

    Map<String, ExcelFile> excelData = new ConcurrentHashMap<>();

    @Override
    public ExcelFile getFileById(String id) {
        if(excelData.containsKey(id)){
            return excelData.get(id);
        }
        return null;
    }

    @Override
    public String saveFile(String id, String abs_path) {
        if(excelData.containsKey(id)){
        }
        else{
            ExcelFile excelFile = new ExcelFile();
            excelFile.setFile(abs_path);
            excelData.put(id, excelFile);
        }
        return id;
    }

    @Override
    public ExcelFile deleteFile(String id) {
        if(excelData.containsKey(id)){
            return excelData.remove(id);
        }
        return null;
    }

    @Override
    public List<ExcelFileIdAndPath> getFiles() {
        if(excelData == null || excelData.size() == 0){
            return null;
        }
        List<ExcelFileIdAndPath> res = new ArrayList<>();
        for( String key : excelData.keySet()){
            ExcelFileIdAndPath excelFileIdAndPath = new ExcelFileIdAndPath();
            ExcelFile tmp = excelData.get(key);
            excelFileIdAndPath.setFileID(key);
            excelFileIdAndPath.setFilePath(tmp.getFile());
            res.add(excelFileIdAndPath);
        }
        return res;
    }
}

