package edu.java.scrapper.controller;

import edu.java.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice(basePackages = {"edu.java.scrapper.controller"})
public class ScrapperExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiErrorResponse> badRequestException(BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                e.getCause().toString(),
                e.getMessage(),
                (String[]) Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray())
            );
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiErrorResponse> notFoundException(HttpClientErrorException.NotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                "Не найдено",
                "404",
                e.getCause().toString(),
                e.getMessage(),
                (String[]) Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray())
            );
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ApiErrorResponse> conflictException(HttpClientErrorException.Conflict e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                "Уже существует",
                "409",
                e.getCause().toString(),
                e.getMessage(),
                (String[]) Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray())
            );
    }
}
