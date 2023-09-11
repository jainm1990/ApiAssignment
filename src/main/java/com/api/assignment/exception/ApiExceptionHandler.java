package com.api.assignment.exception;

import com.api.assignment.constants.UserResponseStatusEnum;
import com.api.assignment.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserResponse> handleInvalidArguments(MethodArgumentNotValidException ex) {

        StringBuilder error = new StringBuilder();
        logger.info("Inside handleInvalidArguments handler {} ", ex.getMessage());

        for (FieldError fieldError : ex.getFieldErrors())
            error.append(fieldError.getDefaultMessage()).append(", ");
        if (error.length() >= 2) {
            error.delete(error.length() - 2, error.length());
        }
        UserResponse response = new UserResponse();
        response.setResponseCode(UserResponseStatusEnum.INVAID_REQUEST_PARAMETERS.getMessageCode().toString());
        response.setStatus(UserResponseStatusEnum.INVAID_REQUEST_PARAMETERS.getStatus());
        //response.setResponseMessage(OnboardResponseStatus.INVAID_REQUEST_PARAMETERS.getDescription());
        response.setResponseMessage(error.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<UserResponse> handleuserExists(UserAlreadyExistException ex) {

        UserResponse response = new UserResponse();
        response.setResponseCode(UserResponseStatusEnum.REQUEST_FAILED_DUPLICATE_RECORD.getMessageCode().toString());
        response.setStatus(UserResponseStatusEnum.REQUEST_FAILED_DUPLICATE_RECORD.getStatus());
        //response.setResponseMessage(OnboardResponseStatus.INVAID_REQUEST_PARAMETERS.getDescription());
        response.setResponseMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponse> handleAll(Exception ex) {

        UserResponse response = new UserResponse(UserResponseStatusEnum.REQUEST_FAILED.getMessageCode().toString(),
                ex.getMessage(),
                UserResponseStatusEnum.REQUEST_FAILED.getStatus(),
                new Date());


        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }


}
