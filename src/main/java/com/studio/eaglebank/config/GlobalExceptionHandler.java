package com.studio.eaglebank.config;

import com.studio.eaglebank.config.constants.ErrorDetails;
import com.studio.eaglebank.config.constants.ErrorObject;
import com.studio.eaglebank.config.exceptions.ForbiddenResourceException;
import com.studio.eaglebank.config.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenResourceException.class)
    public ResponseEntity<String> handleForbiddenResourceException(ForbiddenResourceException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationError(MethodArgumentNotValidException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorObject(
                        "The request didn't supply all the necessary data", getFieldErrors(ex)
                ));
    }

    private static List<ErrorDetails> getFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetails(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getCode()
                ))
                .toList();
    }
}
