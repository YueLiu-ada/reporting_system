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

    // 保存文件并且返回id
    @Override
    public String saveFile(String id, String abs_path) {
        // 如果return nll 就是没成功 如果return string 不是 null 就是成功。
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
        //System.out.println(excelData.size());
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

    // 展示出所有的file
    @Override
    public List<ExcelFileIdAndPath> getFiles() {
        if(excelData.size() == 0){
            //System.out.println("size is null");

            throw new NullRepoException("you haven't save any files. The list is empty now");
        }
        List<ExcelFileIdAndPath> res = new ArrayList<>();
        // 遍历map并新建新的ExcelFileIdAndPath对象 把他们加入到list里面
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

