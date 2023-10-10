package de.iav.backend.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    @ExceptionHandler(TimeSlotNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTimeSlotNotFoundException(TimeSlotNotFoundException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", exception.getMessage());
        body.put(TIMESTAMP, Instant.now().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFoundException(UserNotFoundException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", exception.getMessage());
        body.put(TIMESTAMP, Instant.now().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationException(BindException ex) {
        Map<String, Object> responseBody = new HashMap<>();

        for (FieldError error : ex.getFieldErrors()) {
            responseBody.put(error.getField(), error.getDefaultMessage());
            responseBody.put(TIMESTAMP, Instant.now());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}