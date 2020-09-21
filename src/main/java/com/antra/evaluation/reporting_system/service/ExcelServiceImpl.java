package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Override
    public String CreateZipFile(List<String> ids) throws FileNotFoundException {
        List<String> absolutePath = new ArrayList<>();
        for(String id : ids){
            ExcelFile excelFile = excelRepository.getFileById(id);
            if(excelFile != null){
                absolutePath.add(excelFile.getFile());
            }
        }
        String zipFile = System.getProperty("user.dir") + "/" + LocalDateTime.now() + ".zip";
        String[] srcFiles = absolutePath.toArray(new String[absolutePath.size()]);
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i=0; i < srcFiles.length; i++) {
                File srcFile = new File(srcFiles[i]);
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            return zipFile;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
