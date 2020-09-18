package com.antra.evaluation.reporting_system.exception;

// if repository is null throw this exception
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
