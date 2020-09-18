package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;

import java.util.List;
import java.util.Optional;

public interface ExcelRepository {
    Optional<ExcelFile> getFileById(String id);

    String saveFile(String id, String abs_path);

    ExcelFile deleteFile(String id);

    List<ExcelFileIdAndPath> getFiles();


}
