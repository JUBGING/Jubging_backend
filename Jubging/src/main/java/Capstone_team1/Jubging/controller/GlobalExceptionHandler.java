package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ConflictException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import Capstone_team1.Jubging.config.exception.NotFoundException;
import Capstone_team1.Jubging.config.exception.UnauthorizedException;
import Capstone_team1.Jubging.dto.exception.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //@Valid or @Validated 바인딩 에러시 발생
    //주로 @RequestBody, @RequestPart 어노테이션에서 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        GlobalExceptionHandler.log.error("handleMethodArgumentNotValidException", exception);
        return new ExceptionDto(ErrorCode.INVALID_INPUT_VALUE, "입력 양식이 틀립니다.");
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionDto handleBindException(BindException exception) {
        GlobalExceptionHandler.log.error("handleBindException", exception);
        return new ExceptionDto(ErrorCode.INVALID_INPUT_VALUE, "입력 타입이 틀립니다");
    }
    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadRequestException(BadRequestException exception) {
        GlobalExceptionHandler.log.error("error message", exception);
        return new ExceptionDto(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadHttpRequestMethodException(HttpRequestMethodNotSupportedException exception) {
        GlobalExceptionHandler.log.error("error message", exception);
        return new ExceptionDto(ErrorCode.INVALID_HTTP_METHOD, "Invalid request http method (GET, POST, PUT, DELETE)");
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ExceptionDto handleUnauthorizedException(UnauthorizedException exception) {
        GlobalExceptionHandler.log.error("error message", exception);
        return new ExceptionDto(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ExceptionDto handleNotFoundException(NotFoundException exception) {
        GlobalExceptionHandler.log.error("error message", exception);
        return new ExceptionDto(exception.getErrorCode(), exception.getMessage());
    }
    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ExceptionDto handleConflictException(ConflictException exception) {
        GlobalExceptionHandler.log.error("error message", exception);
        return new ExceptionDto(exception.getErrorCode(), exception.getMessage());
    }
}
