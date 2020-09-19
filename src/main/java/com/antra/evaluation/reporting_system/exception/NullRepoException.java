package com.antra.evaluation.reporting_system.exception;

public class NullRepoException extends RuntimeException{
    private String errorMsg;

    public NullRepoException(String msg){
        this.errorMsg = msg;
    }
    public String getErrorMsg(){
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
