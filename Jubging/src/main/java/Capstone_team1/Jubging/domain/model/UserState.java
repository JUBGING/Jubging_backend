package Capstone_team1.Jubging.domain.model;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserState {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    REJECT("REJECT"),
    LEAVE("LEAVE");

    private final String value;

    UserState(String value) {
        this.value = value;
    }

    public static UserState of(String value) {
        return Arrays.stream(values())
                .filter(v -> value.equalsIgnoreCase(v.value))
                .findFirst()
                .orElseThrow(
                        () -> new BadRequestException(
                                ErrorCode.INVALID_REQUEST_USER_STATE,
                                String.format("'%s' is invalid : not supported", value)
                        )
                );
    }
}
