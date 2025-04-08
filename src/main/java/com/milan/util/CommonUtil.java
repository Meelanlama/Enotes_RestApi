package com.milan.util;

import com.milan.handler.GenericResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public class CommonUtil {

    public static ResponseEntity<?> createBuildResponse(Object data,HttpStatus status){

        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("Success")
                .message("Success")
                .data(data)
                .build();
        //calling create method of GenericResponse
        return response.create();
    }

    //send only message, not data. after saving show success msg only
    public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {

        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("Success")
                .message(message)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponse(Object data,HttpStatus status){

        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("Failure")
                .message("Failure")
                .data(data)
                .build();
        //calling create method of GenericResponse
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {

        GenericResponse response = GenericResponse.builder()
                .responseStatus(status)
                .status("Failure")
                .message(message)
                .build();
        return response.create();
    }

    public static String getContentType(String originalFileName) {
            // Extract file extension using Apache Commons IO (dependency)
            String extension = FilenameUtils.getExtension(originalFileName);

            switch (extension) {
                case "pdf":
                    return "application/pdf";
                case "xlsx":
                    return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
                case "txt":
                    return "text/plain";
                case "png":
                    return "image/png";
                case "jpeg":
                    return "image/jpeg";
                case "docx":
                    return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                default:
                    return "application/octet-stream"; // Default MIME type for unknown files
            }
        }
    }

