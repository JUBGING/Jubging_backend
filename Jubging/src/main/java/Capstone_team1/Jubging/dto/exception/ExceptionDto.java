package Capstone_team1.Jubging.dto.exception;

import Capstone_team1.Jubging.config.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionDto {
    private final int errorCode;
    private final String message;
    private final LocalDateTime timeStamp;

    public ExceptionDto(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.getCode();
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}