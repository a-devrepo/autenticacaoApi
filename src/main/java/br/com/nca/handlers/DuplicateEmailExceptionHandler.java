package br.com.nca.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.nca.domain.dtos.ApiErrorResponse;
import br.com.nca.domain.exceptions.DuplicateEmailException;

@RestControllerAdvice
public class DuplicateEmailExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmailException(DuplicateEmailException exception,
            WebRequest request) {

        var body = ApiErrorResponse
                .builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
