package com.antra.evaluation.reporting_system.exception;

// 当generate 文件或者是存储文件失败的时候 捕获异常
public class FileCannotSaveException extends RuntimeException{
    //private int errorCode;
    private String errorMsg;

    public FileCannotSaveException(String msg){
        this.errorMsg = msg;
    }

    public String getErrorMsg(){
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }

}
