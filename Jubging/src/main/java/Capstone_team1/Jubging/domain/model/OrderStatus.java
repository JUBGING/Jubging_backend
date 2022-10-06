package Capstone_team1.Jubging.domain.model;

import Capstone_team1.Jubging.config.exception.BadRequestException;
import Capstone_team1.Jubging.config.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderStatus {

    READY("READY"),
    SHIPPING("SHIPPING"),
    COMPLETE("COMPLETE");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
