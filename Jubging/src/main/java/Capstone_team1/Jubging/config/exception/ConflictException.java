package Capstone_team1.Jubging.config.exception;

public class ConflictException extends BaseRuntimeException{
    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
