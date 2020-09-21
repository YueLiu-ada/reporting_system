package com.antra.evaluation.reporting_system.service;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    InputStream getExcelBodyById(String id) throws FileNotFoundException;
    boolean delExcelFileById(String id) throws IOException, Exception;
    List<ExcelFileIdAndPath> listExistingFiles();
    String CreateZipFile(List<String> ids) throws FileNotFoundException;
}
