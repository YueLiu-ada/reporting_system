package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.exception.NullRepoException;
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
    public Optional<ExcelFile> getFileById(String id) {
        return Optional.ofNullable(excelData.get(id));
    }

    // save file and return id
    @Override
    public String saveFile(String id, String abs_path) {
        // return null fail, return a string success
        if(excelData.containsKey(id)){
            //System.out.println("already has id in map");
        }
        else{
            ExcelFile excelFile = new ExcelFile();
            excelFile.setFile(abs_path);
            System.out.println(abs_path);
            excelData.put(id, excelFile);
            System.out.println(excelData.get(id));
        }
        return id;
    }

    @Override
    public ExcelFile deleteFile(String id) {
        if(excelData.containsKey(id)){
            // if it exists, delete
            //ExcelFile tmp = excelData.get(id);
            // actually return file's abs_path and then delete.
            return excelData.remove(id);
        }
        return null;
    }

    // list all files
    @Override
    public List<ExcelFileIdAndPath> getFiles() {
        if(excelData == null || excelData.size() == 0){
            return null;
        }
        List<ExcelFileIdAndPath> res = new ArrayList<>();
        // traverse map and create new ExcelFileIdAndPath obj  add them to list
        for( String key : excelData.keySet()){
            ExcelFileIdAndPath excelFileIdAndPath = new ExcelFileIdAndPath();
            ExcelFile tmp = excelData.get(key);
            excelFileIdAndPath.setFileID(key);
            //System.out.println(key);
            excelFileIdAndPath.setFilePath(tmp.getFile());
            //System.out.println(tmp.getFile());
            res.add(excelFileIdAndPath);
        }
        return res;
    }
}

