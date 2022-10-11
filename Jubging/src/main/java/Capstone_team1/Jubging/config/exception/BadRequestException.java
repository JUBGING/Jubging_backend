package Capstone_team1.Jubging.config.exception;

public class BadRequestException extends BaseRuntimeException{
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
