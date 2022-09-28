package Capstone_team1.Jubging.config.exception;

public class OAuthProviderMissMatchException extends BaseRuntimeException {

    public OAuthProviderMissMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}