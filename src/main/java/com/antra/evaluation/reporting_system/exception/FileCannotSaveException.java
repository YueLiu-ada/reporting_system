package com.antra.evaluation.reporting_system.exception;

// when fail to generate and save file, throw this exception
public class FileCannotSaveException extends RuntimeException{

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
