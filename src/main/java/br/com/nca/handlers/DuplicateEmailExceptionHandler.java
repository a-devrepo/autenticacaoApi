package br.com.nca.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.nca.domain.exceptions.AccessDeniedException;
import br.com.nca.domain.exceptions.DuplicateEmailException;

@RestControllerAdvice
public class DuplicateEmailExceptionHandler {
    
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(
            DuplicateEmailException exception,
            WebRequest request) {
        
        var body = new HashMap<String, Object>();
        
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("message", exception.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
