package com.antra.evaluation.reporting_system.pojo.report;

public class ReturnExcelFileType {
    private String fileId;
    private String abs_path;
    private String complete_time;
    private String file_size;

    public ReturnExcelFileType(){}

    public ReturnExcelFileType(String fileId,String abs_path,String complete_time,String file_size){
        this.fileId = fileId;
        this.abs_path = abs_path;
        this.complete_time = complete_time;
        this.file_size = file_size;
    }


    public String getFileId(){
        return fileId;
    }

    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    public String getAbs_path(){
        return abs_path;
    }

    public void setAbs_path(String abs_path){
        this.abs_path = abs_path;
    }

    public String getComplete_time(){
        return complete_time;
    }

    public void setComplete_time(String complete_time){
        this.complete_time = complete_time;
    }

    public String getFile_size(){
        return file_size;
    }

    public void setFile_size(String file_size){
        this.file_size = file_size;
    }
}
