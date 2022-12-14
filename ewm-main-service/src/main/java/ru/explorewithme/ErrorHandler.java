package ru.explorewithme;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.explorewithme.exception.ApiError;
import ru.explorewithme.exception.IdException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice()
@Slf4j
public class ErrorHandler {
    //400
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiError.builder()
                .errors(null)
                .message(e.getMessage())
                .reason(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    //400
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        return ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    //400
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiError.builder()
                .errors(Arrays.asList(e.getStackTrace()))
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    //500
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePSQLExceptionException(PSQLException e) {
        return ApiError.builder()
                .errors(null)
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    //404
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleIdException404(IdException e) {
        log.error(e.getMessage());
        return ApiError.builder()
                .errors(null)
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return ApiError.builder()
                //Arrays.asList(e.getStackTrace())
                .errors(null)
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
