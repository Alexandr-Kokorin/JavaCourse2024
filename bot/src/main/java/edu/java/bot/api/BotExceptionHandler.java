package edu.java.bot.api;

import edu.java.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.bot.api"})
public class BotExceptionHandler {

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
}
