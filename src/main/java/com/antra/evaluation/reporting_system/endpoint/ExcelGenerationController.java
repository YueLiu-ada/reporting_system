package com.antra.evaluation.reporting_system.endpoint;
import com.antra.evaluation.reporting_system.exception.NullRepoException;
import com.antra.evaluation.reporting_system.exception.deleteNonExistFileException;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFileIdAndPath;
import com.antra.evaluation.reporting_system.pojo.report.ReturnExcelFileType;
import com.antra.evaluation.reporting_system.service.ExcelGenerationService;
import com.antra.evaluation.reporting_system.service.ExcelService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class ExcelGenerationController {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerationController.class);

    ExcelService excelService;
    ExcelGenerationService excelGenerationService;

    @Autowired
    public ExcelGenerationController(ExcelService excelService, ExcelGenerationService excelGenerationService) {
        this.excelService = excelService;
        this.excelGenerationService = excelGenerationService;
    }



    // Generate a new excel file and return responseEntity
    //return  excel meta info which includes fileID，generatedTime， filesize， download link
    // pass parameters into request change into exceldata的形式
    // create a file by using excel service
    @PostMapping("/excel")
    @ApiOperation("Generate Excel")

    public ResponseEntity<ExcelResponse> createExcel(@Valid @RequestBody ExcelRequest request) throws IOException {

        ExcelResponse response = new ExcelResponse();
        ReturnExcelFileType reft = excelGenerationService.generateAndSaveExcelFile(request);
        if(reft == null){
            //throw new FileNotFoundException("cannot find file");
            return new ResponseEntity<ExcelResponse>(response, HttpStatus.BAD_REQUEST);
        }
        else{
            response.setRespMsg("already created and saved");
            response.setFileId(reft.getFileId());
            response.setFile_path(reft.getAbs_path());
            response.setComplete_time(reft.getComplete_time());
            response.setFile_size(reft.getFile_size());
            return new ResponseEntity<ExcelResponse>(response, HttpStatus.OK);
        }
    }

    // create split file by using excel service
    @PostMapping("/excel/auto")
    @ApiOperation("Generate Multi-Sheet Excel Using Split field")
    public ResponseEntity<ExcelResponse> createMultiSheetExcel(@Valid @RequestBody MultiSheetExcelRequest request) throws IOException {
        ExcelResponse response = new ExcelResponse();
        ReturnExcelFileType reft = excelGenerationService.generateAndSaveMultiSheetsExcelFile(request);
        if(reft == null){
            throw new FileNotFoundException("cannot find file");
        }
        else{
            response.setRespMsg("file already created and saved");
            response.setFileId(reft.getFileId());
            response.setFile_path(reft.getAbs_path());
            response.setComplete_time(reft.getComplete_time());
            response.setFile_size(reft.getFile_size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // this is used to list all the file that you provided
    @GetMapping("/excel")
    @ApiOperation("List all existing files")
    public ResponseEntity<List<ExcelFileIdAndPath>> listExcels() {
        //var response = new ArrayList<ExcelFileIdAndPath>();
        var response = excelService.listExistingFiles();
        if(response == null){
            throw new NullRepoException("you haven't saved anything");
            //return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //  already implemented
    // download excel file   by using  ExcelService
    @GetMapping("/excel/{id}/content")
    public void downloadExcel(@PathVariable String id, HttpServletResponse response) throws IOException {
        InputStream fis = excelService.getExcelBodyById(id);
        response.setHeader("Content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=\"name_of_excel_file.xls\""); // TODO: File name cannot be hardcoded here
        FileCopyUtils.copy(fis, response.getOutputStream());
    }

    // delete excel file
    // return delete response
    @DeleteMapping("/excel/{id}")
    public ResponseEntity<ExcelResponse> deleteExcel(@PathVariable String id) throws Exception {
        var response = new ExcelResponse();
        boolean del = excelService.delExcelFileById(id);
        if(del == true){
            response.setFileId(id);
            response.setRespMsg("Already delete");
        }
        else{
            //response.setFileId("");
            //response.setRespMsg("Cannot delete");
            throw new deleteNonExistFileException("non exist file");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // for test
    @PostMapping("/testexcel")
    @ApiOperation("Generate Excel")
    public ResponseEntity<ExcelResponse> testExcel(@RequestBody ExcelRequest request)  {

        ExcelResponse response = new ExcelResponse();
        boolean res = excelGenerationService.TestCase(100);
        response.setRespMsg("true");
        return new ResponseEntity<ExcelResponse>(response, HttpStatus.OK);

    }
}
// Log
// Exception handling
// Validation
