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
}
