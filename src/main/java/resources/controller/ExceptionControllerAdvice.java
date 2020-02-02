package resources.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import resources.exceptions.BadRequestException;
import resources.exceptions.ErrorResponse;
import resources.exceptions.InvalidCredentialsException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<?> handleException(InvalidCredentialsException exception) {
        return responseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleException(BadRequestException exception) {
        return responseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<?> handleException(SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.toString(), exception.getClass()));
    }

    private ResponseEntity<?> responseEntity(HttpStatus httpStatus, RuntimeException e) {
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(e.getMessage(), httpStatus.toString(), e.getClass()));
    }

}
