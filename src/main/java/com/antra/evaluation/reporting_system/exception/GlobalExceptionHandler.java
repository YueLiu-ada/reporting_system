package com.antra.evaluation.reporting_system.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerNullPointer(NullPointerException e){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.ordinal()); // 不确定
        error.setErrorMsg(e.getMessage());
        logger.error("Controller Error: the list is empty now, please save something first ! ",e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = FileCannotSaveException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerFileNotSaved(FileCannotSaveException e) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorMsg(e.getErrorMsg());
        error.setErrorCode(454);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value =deleteNonExistFileException.class )
    public ResponseEntity<ErrorResponse> exceptionHandlerDeleteNonExistFile(deleteNonExistFileException e){
        ErrorResponse error = new ErrorResponse();
        error.setErrorMsg(e.getErrorMsg());
        error.setErrorCode(455);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = SpliterCannotFindException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerSpliterNotFound(SpliterCannotFindException e) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorMsg(e.getErrorMsg());
        error.setErrorCode(456);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (value = NullRepoException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerNullRepo(NullRepoException e){
        ErrorResponse error = new ErrorResponse();
        error.setErrorMsg(e.getErrorMsg());
        error.setErrorCode(457);
        logger.error("Controller Error: the list is empty now, please save something first ! ",e);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (value = MethodArgumentNotValid.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerArguNotValid(MethodArgumentNotValid e){
        ErrorResponse error = new ErrorResponse();
        error.setErrorMsg(e.getErrorMsg());
        error.setErrorCode(458);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler ( value = Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setErrorMsg("You just passed invalid values!!!  " + e.getMessage());
        logger.error("Controller Error",e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
