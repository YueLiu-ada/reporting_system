package com.antra.evaluation.reporting_system.exception;

// throw up this exception when you delete non-existing files
public class deleteNonExistFileException extends RuntimeException{
    private String errorMsg;

    public deleteNonExistFileException(String msg){
        this.errorMsg = msg;
    }
    public String getErrorMsg(){
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
