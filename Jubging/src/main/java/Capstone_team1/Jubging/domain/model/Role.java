package Capstone_team1.Jubging.domain.model;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
