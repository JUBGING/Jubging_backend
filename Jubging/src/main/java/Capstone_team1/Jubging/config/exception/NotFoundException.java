package Capstone_team1.Jubging.config.exception;

public class NotFoundException extends BaseRuntimeException{
    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
