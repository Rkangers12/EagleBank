package com.studio.eaglebank.config;

import com.studio.eaglebank.config.constants.ErrorDetails;
import com.studio.eaglebank.config.constants.ErrorObject;
import com.studio.eaglebank.config.constants.ErrorResponse;
import com.studio.eaglebank.config.exceptions.DuplicateResourceException;
import com.studio.eaglebank.config.exceptions.ForbiddenResourceException;
import com.studio.eaglebank.config.exceptions.UnauthorisedException;
import com.studio.eaglebank.config.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenResourceException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenResourceException(ForbiddenResourceException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorisedException(UnauthorisedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
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
