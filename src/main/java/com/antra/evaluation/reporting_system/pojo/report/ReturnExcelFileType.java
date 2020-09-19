package com.antra.evaluation.reporting_system.pojo.report;

public class ReturnExcelFileType {
    private String fileId;
    private String absPath;
    private String completeTime;
    private String fileSize;

    public ReturnExcelFileType(){}

    public ReturnExcelFileType(String fileId,String absPath,String completeTime,String fileSize){
        this.fileId = fileId;
        this.absPath = absPath;
        this.completeTime = completeTime;
        this.fileSize = fileSize;
    }


    public String getFileId(){
        return fileId;
    }

    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    public String getAbsPath(){
        return absPath;
    }

    public void setAbsPath(String absPath){
        this.absPath = absPath;
    }

    public String getCompleteTime(){
        return completeTime;
    }

    public void setCompleteTime(String completeTime){
        this.completeTime = completeTime;
    }

    public String getFileSize(){
        return fileSize;
    }

    public void setFileSize(String fileSize){
        this.fileSize = fileSize;
    }
}
