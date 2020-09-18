package com.antra.evaluation.reporting_system.pojo.api;

public class ExcelResponse {
    private String fileId;
    private String respMsg;
    private String file_path;
    private String complete_time;
    private String file_size;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getRespMsg() { return respMsg; }

    public void setRespMsg(String respMsg) { this.respMsg = respMsg; }

    public String getFile_path(){
        return file_path;
    }

    public void setFile_path(String file_path){
        this.file_path = file_path;
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
