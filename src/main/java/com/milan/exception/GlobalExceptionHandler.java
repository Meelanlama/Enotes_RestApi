package com.milan.exception;

import com.milan.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("In GlobalExceptionHandler :: handleException ::", e.getMessage());
       // return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        log.error("In GlobalExceptionHandler :: handleNullPointerException ::", e.getMessage());
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("In GlobalExceptionHandler :: handleResourceNotFoundException ::", e.getMessage());
       // return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        log.error("In GlobalExceptionHandler :: handleValidationException ::");
        //return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?> handleExistDataException(ExistDataException e) {
        log.error("In GlobalExceptionHandler :: handleExistDataException ::");
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentExceptionException(IllegalArgumentException e) {
        log.error("In GlobalExceptionHandler :: handleIllegalArgumentExceptionException ::", e.getMessage());
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
        log.error("In GlobalExceptionHandler :: handleFileNotFoundException ::", e.getMessage());
        //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> handleSuccessException(SuccessException e) {
        log.error("In GlobalExceptionHandler :: SuccessException ::", e.getMessage());
        return CommonUtil.createBuildResponse(e.getMessage(), HttpStatus.OK);
    }

}
