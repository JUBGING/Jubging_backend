package Capstone_team1.Jubging.domain.model;

import lombok.Getter;

@Getter
public enum TongStatus {
    OCCUPIED("OCCUPIED"),
    UNOCCUPIED("UNOCCUPIED");

    private final String value;

    TongStatus(String value) {
        this.value = value;
    }
}
