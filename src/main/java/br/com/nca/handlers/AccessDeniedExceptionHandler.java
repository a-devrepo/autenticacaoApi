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

@RestControllerAdvice
public class AccessDeniedExceptionHandler {
 
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccesDeniedException(
            AccessDeniedException exception,
            WebRequest request) {
        
        var body = new HashMap<String, Object>();
        
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", exception.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
