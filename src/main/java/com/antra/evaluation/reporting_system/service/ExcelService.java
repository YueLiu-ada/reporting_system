package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import io.swagger.models.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    InputStream getExcelBodyById(String id);
    boolean delExcelFileById(String id) throws IOException, Exception;
    List<ExcelFileIdAndPath> listExistingFiles();
}
