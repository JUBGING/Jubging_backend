package Capstone_team1.Jubging.config.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 400 Bad Request
     */
    NEED_SIGN_IN(4000),
    INVALID_HTTP_METHOD(4001),
    INVALID_INPUT_VALUE(4002),
    DUPLICATE_EMAIL(4003),
    INCORRECT_STATUS(4004),
    ALREADY_OCCUPIED_TONG(4005),
    UNAVAILABLE_JUBJUBI(4006),
    /**
     * 401 Unauthorized
     */
    REJECT_USER(4010),
    INACTIVE_USER(4011),
    INVALID_ACCESS_JWT(4012),
    INVALID_REQUEST_ROLE(4013),
    INVALID_REQUEST_USER_STATE(4014),

    INVALID_EMAIL(4015),
    INVALID_REFRESH_JWT(4016),
    LEAVE_USER(4017),
    INVALID_REQUEST_ORDERSTATUS(4018),
    INVALID_PASSWORD(4019),

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND_USER(4040),
    NOT_FOUND_TONG(4041),
    NOT_FOUND_JUBJUBI(4042),
    NOT_FOUND_JUBGING_DATA(4043),
    /**
     * 409 CONFLICT
     */
    CONFLICT(4090),
    DELETE_FAIL(4091),
    UPDATE_FAIL(4092),
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
