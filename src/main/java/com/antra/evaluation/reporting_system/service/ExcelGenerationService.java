package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import com.antra.evaluation.reporting_system.pojo.report.ReturnExcelFileType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ExcelGenerationService {
    File generateExcelReport(ExcelData data) throws IOException;
    ReturnExcelFileType generateAndSaveExcelFile(ExcelRequest request) throws IOException;
    ReturnExcelFileType generateAndSaveMultiSheetsExcelFile(MultiSheetExcelRequest request) throws IOException;
}
