package com.example.demo.exception;

import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({ResourceNotFound.class, ResourceAlreadyPresent.class})
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(RuntimeException ex) {
        ApiResponse response = new ApiResponse(false, ex.getMessage(),ex.getClass().getSimpleName());
        if (ex.getClass().getSimpleName().equals("ResourceAlreadyPresent")){
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse response = new ApiResponse(false, ex.getMessage(),ex.getClass().getSimpleName());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse> handleAccessDeniedException(RuntimeException ex) {
        ApiResponse response = new ApiResponse(false, ex.getMessage(),ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
