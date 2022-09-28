package Capstone_team1.Jubging.config.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 400 Bad Request
     */
    NEED_SIGN_IN(4000),

    /**
     * 401 Unauthorized
     */
    REJECT_USER(4010),
    INACTIVE_USER(4011),
    INVALID_ACCESS_JWT(4012),
    INVALID_REQUEST_ROLE(4013),
    INVALID_REQUEST_USER_STATE(4014),

    INVALID_SIGNIN(4015),
    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND_USER(4040),

    /**
     * 409 CONFLICT
     */
    CONFLICT(4090),
    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER(5000),
    PARSING_ERROR(5001),

    /**
     * 503 Service Unavailable Error
     */
    SERVICE_UNAVAILABLE(5030);

    private int code;

    ErrorCode(int code) {this.code = code;}
}
